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

package io.github.hylexus.xtream.codec.ext.jt1078.extensions.handler;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.DefaultUdpXtreamNettyHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;
import java.util.List;

public class Jt1078ServerUdpHandlerAdapter extends DefaultUdpXtreamNettyHandlerAdapter {
    public Jt1078ServerUdpHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator xtreamExchangeCreator, XtreamHandler xtreamHandler) {
        super(allocator, xtreamExchangeCreator, xtreamHandler);
    }

    protected Mono<Void> handleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, DatagramPacket datagramPacket) {
        final InetSocketAddress remoteAddress = datagramPacket.sender();
        // 一个 UDP 包可能包含多个 JT1078 (完整)报文
        final List<ByteBuf> byteBufList = this.split(datagramPacket.content());

        // 只有一个包(绝大多数场景)
        if (byteBufList.size() == 1) {
            return this.handleSingleRequest(nettyInbound, nettyOutbound, byteBufList.getFirst(), remoteAddress);
        }

        // 每个(完整)包都触发一次处理逻辑
        return Flux.fromIterable(byteBufList).flatMap(byteBuf -> {
            // ...
            return this.handleSingleRequest(nettyInbound, nettyOutbound, byteBuf, remoteAddress);
        }).then();
    }

    private List<ByteBuf> split(ByteBuf content) {
        // todo 自定义分包器
        return List.of(content);
    }

    protected Mono<Void> handleSingleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf payload, InetSocketAddress remoteAddress) {
        final XtreamExchange exchange = this.exchangeCreator.createUdpExchange(allocator, nettyInbound, nettyOutbound, payload, remoteAddress);
        return this.doUdpExchange(exchange).doFinally(signalType -> {
            XtreamBytes.releaseBuf(payload);
            exchange.request().release();
        });
    }

}
