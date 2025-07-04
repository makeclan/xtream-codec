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

package io.github.hylexus.xtream.codec.ext.jt1078.codec.impl;


import io.github.hylexus.xtream.codec.common.utils.XtreamByteReader;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.Jt1078RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;

/**
 * @author hylexus
 */
public class DefaultJt1078RequestDecoder implements Jt1078RequestDecoder {
    private final Jt1078Request.Jt1078TraceIdGenerator traceIdGenerator;

    public DefaultJt1078RequestDecoder(Jt1078Request.Jt1078TraceIdGenerator traceIdGenerator) {
        this.traceIdGenerator = traceIdGenerator;
    }

    @Override
    public Jt1078Request decode(boolean hasIdentifier, String requestId, ByteBufAllocator allocator, NettyInbound nettyInbound, XtreamInbound.Type requestType, InetSocketAddress remoteAddress, ByteBuf input) {

        final ByteBuf readable = hasIdentifier
                // +4: 0x30316364 --> 4bytes
                ? input.slice().readerIndex(input.readerIndex() + 4)
                : input.slice();
        try {
            final Jt1078RequestHeader.Jt1078RequestHeaderBuilder headerBuilder = Jt1078RequestHeader.newBuilder();
            final XtreamByteReader reader = XtreamByteReader.of(readable);

            reader.readU8(headerBuilder::offset4);
            reader.readU8(headerBuilder::offset5);
            reader.readU16(headerBuilder::offset6);
            reader.readBcd(6, headerBuilder::offset8);
            reader.readU8(headerBuilder::offset14);

            final short offset15 = reader.readU8();
            headerBuilder.offset15(offset15);

            final byte dataType = Jt1078RequestHeader.dataTypeValue(offset15);
            final boolean hasTimestampField = Jt1078RequestHeader.hasTimestampField(dataType);
            final boolean hasFrameIntervalFields = Jt1078RequestHeader.hasFrameIntervalFields(dataType);

            if (hasTimestampField) {
                reader.readI64(headerBuilder::offset16);
            }

            if (hasFrameIntervalFields) {
                reader.readU16(headerBuilder::offset24);
                reader.readU16(headerBuilder::offset26);
            }

            final int msgLengthFieldIndex = Jt1078RequestHeader.msgLengthFieldIndex(hasIdentifier, hasTimestampField, hasFrameIntervalFields);
            final int bodyLength = reader.readU16();
            headerBuilder.offset28(bodyLength);
            // +2: msgBodyLength --> WORD --> 2bytes
            final ByteBuf body = readable.slice(msgLengthFieldIndex + 2, bodyLength);

            final Jt1078RequestHeader header = headerBuilder.build();
            final String traceId = this.traceIdGenerator.generateTraceId(nettyInbound, header);
            return new DefaultJt1078Request(
                    requestId,
                    traceId, allocator,
                    nettyInbound,
                    requestType,
                    remoteAddress,
                    header,
                    body
            );

        } catch (Throwable e) {
            XtreamBytes.releaseBuf(input);
            throw new RuntimeException(e);
        }
    }

}
