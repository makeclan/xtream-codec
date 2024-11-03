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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.handler;

import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808UdpDatagramPackageSplitter;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.ext.jt808.utils.Jt808AttachmentHandlerUtils;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;
import java.time.Instant;

/**
 * @author hylexus
 */
public class Jt808AttachmentServerUdpHandlerAdapter extends Jt808InstructionServerUdpHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Jt808AttachmentServerUdpHandlerAdapter.class);
    protected final XtreamHandler attachmentHandler;

    public Jt808AttachmentServerUdpHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator exchangeCreator, XtreamHandler xtreamHandler, Jt808UdpDatagramPackageSplitter splitter, XtreamHandler attachmentHandler) {
        super(allocator, exchangeCreator, xtreamHandler, splitter);
        this.attachmentHandler = attachmentHandler;
    }

    @Override
    protected Mono<Void> handleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, DatagramPacket datagramPacket) {
        // 普通的指令消息
        if (!JtProtocolUtils.isAttachmentRequest(datagramPacket.content())) {
            return super.handleRequest(nettyInbound, nettyOutbound, datagramPacket);
        }

        // 码流消息
        return this.handleStreamRequest(nettyInbound, nettyOutbound, datagramPacket.content(), datagramPacket.sender());
    }

    protected Mono<Void> handleStreamRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf payload, InetSocketAddress remoteAddress) {
        final Jt808Session session = Jt808AttachmentHandlerUtils.getAttachmentSession(nettyOutbound);
        if (session == null) {
            return Mono.error(new IllegalStateException("attachment session not found"));
        }

        session.lastCommunicateTime(Instant.now());

        final XtreamExchange exchange = this.exchangeCreator.createTcpExchange(allocator, nettyInbound, nettyOutbound, payload, remoteAddress);
        final Jt808Request jt808Request = Jt808AttachmentHandlerUtils.simulateJt808Request(allocator, nettyInbound, payload, session, exchange, exchangeCreator.generateRequestId(nettyInbound));
        final XtreamExchange simulatedExchange = exchange.mutate().request(jt808Request).build();
        return this.attachmentHandler.handle(simulatedExchange);
    }
}
