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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;

public class DefaultXtreamExchangeCreator implements XtreamExchangeCreator {

    @Override
    public XtreamExchange createTcpExchange(ByteBufAllocator allocator, NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf byteBuf) {
        final XtreamRequest.Type type = XtreamRequest.Type.TCP;
        final XtreamSession session = new DefaultXtreamSession(type);
        final DefaultXtreamRequest request = new DefaultXtreamRequest(allocator, nettyInbound, Mono.just(session), byteBuf);
        final InetSocketAddress remoteAddress = request.remoteAddress();

        return new DefaultXtreamExchange(
                request,
                new DefaultXtreamResponse(allocator, nettyOutbound, type, remoteAddress),
                session
        );
    }

    @Override
    public XtreamExchange createUdpExchange(ByteBufAllocator allocator, NettyInbound nettyInbound, NettyOutbound nettyOutbound, DatagramPacket datagramPacket) {
        final InetSocketAddress remoteAddr = datagramPacket.sender();
        final XtreamRequest.Type type = XtreamRequest.Type.UDP;
        final XtreamSession session = new DefaultXtreamSession(type);

        return new DefaultXtreamExchange(
                new DefaultXtreamRequest(allocator, nettyInbound, Mono.just(session), datagramPacket),
                new DefaultXtreamResponse(allocator, nettyOutbound, type, remoteAddr),
                session
        );
    }
}
