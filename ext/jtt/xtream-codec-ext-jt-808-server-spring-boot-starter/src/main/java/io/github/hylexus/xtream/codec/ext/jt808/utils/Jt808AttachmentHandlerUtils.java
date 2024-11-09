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

package io.github.hylexus.xtream.codec.ext.jt808.utils;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ServerType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808MessageBodyProps;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AttributeKey;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;

/**
 * @author hylexus
 */
public final class Jt808AttachmentHandlerUtils {
    private Jt808AttachmentHandlerUtils() {
    }

    public static Jt808Session getAttachmentSessionUdp(NettyOutbound outbound, InetSocketAddress remoteAddress) {
        final Jt808Session[] sessionHolder = new Jt808Session[1];
        outbound.withConnection(connection -> {
            final AttributeKey<Jt808Session> key = JtProtocolConstant.udpSessionKey(remoteAddress);
            final Jt808Session jt808Session = connection.channel().attr(key).get();
            sessionHolder[0] = jt808Session;
        });
        return sessionHolder[0];
    }

    public static Jt808Session getAttachmentSessionTcp(NettyOutbound outbound) {
        final Jt808Session[] sessionHolder = new Jt808Session[1];
        outbound.withConnection(connection -> {
            final AttributeKey<Jt808Session> key = JtProtocolConstant.tcpSessionKey();
            final Jt808Session jt808Session = connection.channel().attr(key).get();
            sessionHolder[0] = jt808Session;
        });
        return sessionHolder[0];
    }

    /**
     * 将苏标扩展的码流消息(0x30316364)模拟为普通的指令消息，方便通过注解的方式处理请求。
     */
    public static Jt808Request simulateJt808Request(ByteBufAllocator allocator, NettyInbound nettyInbound, ByteBuf payload, Jt808Session session, XtreamExchange exchange, String traceId) {
        final Jt808RequestHeader header = Jt808RequestHeader.newBuilder()
                .version(session.protocolVersion())
                .messageId(0x30316364)
                .messageBodyProps(new DefaultJt808MessageBodyProps(0))
                .terminalId(session.terminalId())
                .flowId(0)
                .build();

        final XtreamRequest originalRequest = exchange.request();
        return new DefaultJt808Request(
                Jt808ServerType.ATTACHMENT_SERVER,
                originalRequest.requestId(),
                traceId,
                allocator,
                nettyInbound,
                originalRequest.type(),
                // 跳过 0x30316364 4字节
                payload.readerIndex(payload.readerIndex() + 4),
                originalRequest.remoteAddress(),
                header,
                0,
                0
        );
    }
}
