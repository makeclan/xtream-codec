/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.ext.jt808.codec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.*;
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
    public ByteBuf encode(Object body, Jt808ProtocolVersion version, String terminalId, Jt808ResponseBody annotation) {
        final Jt808MessageDescriber describer = this.createDescriber(version, terminalId, annotation);
        return this.encode(body, describer);
    }

    @Override
    public ByteBuf encode(Object body, Jt808MessageDescriber describer) {
        if (body instanceof ByteBuf byteBuf) {
            return this.doBuild(describer, byteBuf);
        }
        final ByteBuf bodyBuf = this.encodeBody(body);
        return this.doBuild(describer, bodyBuf);
    }

    protected Jt808MessageDescriber createDescriber(Jt808ProtocolVersion version, String terminalId, Jt808ResponseBody annotation) {
        return new Jt808MessageDescriber(version, terminalId)
                .messageId(annotation.messageId())
                .maxPackageSize(annotation.maxPackageSize())
                .reversedBit15InHeader(annotation.reversedBit15InHeader())
                .encryptionType(annotation.encryptionType());
    }

    protected ByteBuf doBuild(Jt808MessageDescriber describer, ByteBuf body) {
        final int maxPackageSize = describer.check().maxPackageSize();
        final int messageBodyLength = body.readableBytes();
        final Jt808ProtocolVersion version = requireNonNull(describer.version(), "version() is null");
        final int estimatedPackageSize = Jt808RequestHeader.messageBodyStartIndex(version, false) + messageBodyLength + 3;
        if (estimatedPackageSize <= maxPackageSize) {
            if (describer.flowId() < 0) {
                describer.flowId(flowIdGenerator.nextFlowId());
            }
            return this.buildPackage(describer, body, 0, 0, describer.flowId());
        }

        final int subPackageBodySize = maxPackageSize - Jt808RequestHeader.messageBodyStartIndex(version, true) - 3;
        final int subPackageCount = messageBodyLength % subPackageBodySize == 0
                ? messageBodyLength / subPackageBodySize
                : messageBodyLength / subPackageBodySize + 1;

        final CompositeByteBuf allResponseBytes = allocator.compositeBuffer(subPackageCount);
        final int[] flowIds = flowIdGenerator.flowIds(subPackageCount);
        for (int i = 0; i < subPackageCount; i++) {
            final int offset = i * subPackageBodySize;
            final int length = (i == subPackageCount - 1)
                    ? Math.min(subPackageBodySize, messageBodyLength - offset)
                    : subPackageBodySize;
            final ByteBuf bodyData = body.slice(offset, length);
            final CompositeByteBuf subPackage = this.buildPackage(describer, bodyData, subPackageCount, i + 1, flowIds[i]);
            allResponseBytes.addComponents(true, subPackage);
        }
        XtreamBytes.releaseBuf(body);
        return allResponseBytes;
    }

    private CompositeByteBuf buildPackage(Jt808MessageDescriber response, ByteBuf body, int totalSubPackageCount, int currentPackageNo, int flowId) {

        // @see https://github.com/hylexus/jt-framework/issues/82
        body = this.encryptionHandler.encryptResponseBody(response, body);

        final ByteBuf headerBuf = this.encodeMessageHeader(response, body, totalSubPackageCount > 0, totalSubPackageCount, currentPackageNo, flowId);
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

    private ByteBuf encodeBody(Object entity) {
        if (entity instanceof ByteBuf byteBuf) {
            return byteBuf;
        }
        final ByteBuf buffer = allocator.buffer();
        try {
            this.entityCodec.encode(entity, buffer);
        } catch (Throwable e) {
            XtreamBytes.releaseBuf(buffer);
            throw e;
        }
        return buffer;
    }

    private ByteBuf encodeMessageHeader(Jt808MessageDescriber response, ByteBuf body, boolean hasSubPackage, int totalSubPkgCount, int currentSubPkgNo, int flowId) {
        final Jt808ProtocolVersion version = response.version();
        final ByteBuf header = allocator.buffer();
        // bytes[0-2) 消息ID Word
        XtreamBytes.writeWord(header, response.messageId());

        // bytes[2-4) 消息体属性 Word
        final int bodyPropsForJt808 = Jt808RequestHeader.Jt808MessageBodyProps.create(
                body.readableBytes(), response.encryptionType(),
                hasSubPackage, response.version(), response.reversedBit15InHeader()
        );
        XtreamBytes.writeWord(header, bodyPropsForJt808);

        // bytes[4] 协议版本号 byte
        if (version == Jt808ProtocolVersion.VERSION_2019) {
            header.writeByte(response.version().getVersionBit());
        }

        // bytes[5-14) 终端手机号 BCD[10]
        // bytes[4-10) 终端手机号 BCD[6]
        XtreamBytes.writeBcd(header, response.terminalId());

        // bytes[15-17) 消息流水号  Word
        XtreamBytes.writeWord(header, flowId);

        // bytes[17-21) 消息包封装项
        // bytes[12-16) 消息包封装项
        if (hasSubPackage) {
            // 消息总包包数
            XtreamBytes.writeWord(header, totalSubPkgCount);
            // 包序号
            XtreamBytes.writeWord(header, currentSubPkgNo);
        }
        return header;
    }

}
