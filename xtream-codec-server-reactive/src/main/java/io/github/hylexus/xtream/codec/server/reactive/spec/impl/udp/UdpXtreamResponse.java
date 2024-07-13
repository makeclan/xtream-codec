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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp;

import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;

/**
 * @author hylexus
 */
public class UdpXtreamResponse extends AbstractXtreamResponse {
    protected final InetSocketAddress remoteAddress;

    public UdpXtreamResponse(ByteBufAllocator allocator, NettyOutbound delegate, InetSocketAddress remoteAddress) {
        super(delegate, allocator);
        this.remoteAddress = remoteAddress;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends ByteBuf> body) {
        return this.underlyingOutbound()
                .sendObject(Flux.from(body).map(byteBuf -> new DatagramPacket(byteBuf, remoteAddress)))
                .then();
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends ByteBuf>> publisher) {
        return Flux.from(publisher)
                .flatMap(source -> Flux.from(source).map(byteBuf -> new DatagramPacket(byteBuf, remoteAddress)))
                .flatMap(datagramPacket -> this.underlyingOutbound().sendObject(datagramPacket))
                .then();
    }
}
