/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.ext.jt808.codec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.tracker.CodecTracker;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.*;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808SubPackageProps;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class DefaultJt808ResponseEncoder implements Jt808ResponseEncoder {
    private static final Logger log = LoggerFactory.getLogger(DefaultJt808ResponseEncoder.class);
    protected final ByteBufAllocator allocator;
    protected final Jt808FlowIdGenerator flowIdGenerator;
    protected final EntityCodec entityCodec;
    protected final Jt808BytesProcessor messageProcessor;
    protected final Jt808MessageEncryptionHandler encryptionHandler;

    public DefaultJt808ResponseEncoder(ByteBufAllocator allocator, Jt808FlowIdGenerator flowIdGenerator, EntityCodec entityCodec, Jt808BytesProcessor messageProcessor, Jt808MessageEncryptionHandler encryptionHandler) {
        this.allocator = allocator;
        this.flowIdGenerator = flowIdGenerator;
        this.entityCodec = entityCodec;
        this.messageProcessor = messageProcessor;
        this.encryptionHandler = encryptionHandler;
    }

    @Override
    public ByteBuf encode(Object body, Jt808ProtocolVersion version, String terminalId, int flowId, Jt808ResponseBody annotation) {
        final Jt808MessageDescriber describer = this.createDescriber(version, terminalId, flowId, annotation);
        return this.encode(body, describer);
    }

    @Override
    public ByteBuf encode(Object body, Jt808ProtocolVersion version, String terminalId, Jt808ResponseBody annotation) {
        final Jt808MessageDescriber describer = this.createDescriber(version, terminalId, -1, annotation);
        return this.encode(body, describer);
    }

    @Override
    public ByteBuf encode(Object body, Jt808MessageDescriber describer) {
        if (body instanceof ByteBuf byteBuf) {
            return this.doBuild(describer, byteBuf);
        }
        final ByteBuf bodyBuf = this.encodeBody(body, describer.bodyCodecTracker());
        return this.doBuild(describer, bodyBuf);
    }

    protected Jt808MessageDescriber createDescriber(Jt808ProtocolVersion version, String terminalId, int flowId, Jt808ResponseBody annotation) {
        final Jt808MessageDescriber describer = new Jt808MessageDescriber(version, terminalId)
                .messageId(annotation.messageId())
                .maxPackageSize(annotation.maxPackageSize())
                .reversedBit15InHeader(annotation.reversedBit15InHeader())
                .encryptionType(annotation.encryptionType());
        if (flowId >= 0) {
            describer.flowId(flowId);
        }
        return describer;
    }

    private Jt808FlowIdGenerator getFlowIdGenerator(Jt808MessageDescriber describer) {
        return describer.flowIdGenerator() == null
                ? this.flowIdGenerator
                : describer.flowIdGenerator();
    }

    protected ByteBuf doBuild(Jt808MessageDescriber describer, ByteBuf body) {
        final int maxPackageSize = describer.check().maxPackageSize();
        final int messageBodyLength = body.readableBytes();
        final Jt808ProtocolVersion version = requireNonNull(describer.version(), "version() is null");
        final int estimatedPackageSize = Jt808RequestHeader.messageBodyStartIndex(version, false) + messageBodyLength + 3;
        if (estimatedPackageSize <= maxPackageSize) {
            if (describer.flowId() < 0) {
                describer.flowId(this.getFlowIdGenerator(describer).nextFlowId());
            }
            return this.buildPackage(describer, body, 0, 0, describer.flowId());
        }

        final int subPackageBodySize = maxPackageSize - Jt808RequestHeader.messageBodyStartIndex(version, true) - 3;
        final int subPackageCount = messageBodyLength % subPackageBodySize == 0
                ? messageBodyLength / subPackageBodySize
                : messageBodyLength / subPackageBodySize + 1;

        final CompositeByteBuf allResponseBytes = allocator.compositeBuffer(subPackageCount);
        final int[] flowIds = this.getFlowIdGenerator(describer).flowIds(subPackageCount);
        for (int i = 0; i < subPackageCount; i++) {
            final int offset = i * subPackageBodySize;
            final int length = (i == subPackageCount - 1)
                    ? Math.min(subPackageBodySize, messageBodyLength - offset)
                    : subPackageBodySize;
            final ByteBuf bodyData = body.retainedSlice(offset, length);
            final CompositeByteBuf subPackage = this.buildPackage(describer, bodyData, subPackageCount, i + 1, flowIds[i]);
            allResponseBytes.addComponents(true, subPackage);
        }
        XtreamBytes.releaseBuf(body);
        return allResponseBytes;
    }

    private CompositeByteBuf buildPackage(Jt808MessageDescriber response, ByteBuf body, int totalSubPackageCount, int currentPackageNo, int flowId) {
        response.messageCodecTracker().beforeMessageBodyEncrypt(totalSubPackageCount, currentPackageNo, flowId, response, body);
        // @see https://github.com/hylexus/jt-framework/issues/82
        body = this.encryptionHandler.encryptResponseBody(response, body);

        final ByteBuf headerBuf = allocator.buffer();
        final Jt808RequestHeader jt808RequestHeader = this.encodeMessageHeader(response, body, totalSubPackageCount > 0, totalSubPackageCount, currentPackageNo, flowId);
        jt808RequestHeader.encode(headerBuf);
        final CompositeByteBuf compositeByteBuf = allocator.compositeBuffer()
                .addComponent(true, headerBuf)
                .addComponent(true, body);

        final byte checkSum = this.messageProcessor.calculateCheckSum(compositeByteBuf);

        compositeByteBuf.writeByte(checkSum);
        compositeByteBuf.resetReaderIndex();
        if (log.isDebugEnabled()) {
            log.debug("- <<<<<<<<<<<<<<< ({}--{}) {}/{}: 7E{}7E",
                    FormatUtils.toHexString(response.messageId(), 4),
                    compositeByteBuf.readableBytes() + 2,
                    Math.max(currentPackageNo, 1), Math.max(totalSubPackageCount, 1),
                    FormatUtils.toHexString(compositeByteBuf)
            );
        }

        final ByteBuf escaped;
        try {
            escaped = this.messageProcessor.doEscapeForSend(compositeByteBuf);
            response.messageCodecTracker().afterMessageEncoded(totalSubPackageCount, currentPackageNo, flowId, checkSum, response, jt808RequestHeader, compositeByteBuf, escaped);
        } catch (Throwable e) {
            XtreamBytes.releaseBuf(compositeByteBuf);
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("+ <<<<<<<<<<<<<<< ({}--{}) {}/{}: 7E{}7E",
                    FormatUtils.toHexString(response.messageId(), 4),
                    escaped.readableBytes() + 2,
                    Math.max(currentPackageNo, 1), Math.max(totalSubPackageCount, 1),
                    FormatUtils.toHexString(escaped)
            );
        }
        return allocator.compositeBuffer()
                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER))
                .addComponent(true, escaped)
                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER));
    }

    private ByteBuf encodeBody(Object entity, CodecTracker tracker) {
        if (entity instanceof ByteBuf byteBuf) {
            return byteBuf;
        }
        final ByteBuf buffer = allocator.buffer();
        try {
            this.entityCodec.encode(entity, buffer, tracker);
        } catch (Throwable e) {
            XtreamBytes.releaseBuf(buffer);
            throw e;
        }
        return buffer;
    }

    private Jt808RequestHeader encodeMessageHeader(Jt808MessageDescriber response, ByteBuf body, boolean hasSubPackage, int totalSubPkgCount, int currentSubPkgNo, int flowId) {
        return Jt808RequestHeader.newBuilder()
                .version(response.version())
                .messageId(response.messageId())
                .messageBodyProps(Jt808RequestHeader.Jt808MessageBodyProps.newBuilder()
                        .messageBodyLength(body.readableBytes())
                        .encryptionType(response.encryptionType())
                        .hasSubPackage(hasSubPackage)
                        .versionIdentifier(response.version())
                        .reversedBit15(response.reversedBit15InHeader())
                        .build()
                )
                .subPackage(new DefaultJt808SubPackageProps(totalSubPkgCount, currentSubPkgNo))
                .terminalId(response.terminalId())
                .flowId(flowId)
                .build();
    }

}
