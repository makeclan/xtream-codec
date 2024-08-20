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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
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
public class DefaultXtreamResponse implements XtreamResponse {
    protected final NettyOutbound delegate;
    protected final ByteBufAllocator byteBufAllocator;
    protected final InetSocketAddress remoteAddress;
    protected final XtreamRequest.Type type;

    public DefaultXtreamResponse(ByteBufAllocator byteBufAllocator, NettyOutbound delegate, XtreamRequest.Type type, InetSocketAddress remoteAddress) {
        this.delegate = delegate;
        this.byteBufAllocator = byteBufAllocator;
        this.remoteAddress = remoteAddress;
        this.type = type;
    }

    @Override
    public NettyOutbound underlyingOutbound() {
        return this.delegate;
    }

    @Override
    public ByteBufAllocator bufferFactory() {
        return this.byteBufAllocator;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends ByteBuf> body) {
        if (this.type == XtreamRequest.Type.TCP) {
            return this.delegate.send(body).then();
        }
        return this.writeWithUdp(body);
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends ByteBuf>> publisher) {
        if (this.type == XtreamRequest.Type.TCP) {
            return this.delegate.sendGroups(Flux.from(publisher)).then();
        }
        return this.writeAndFlushWithUdp(publisher);
    }

    public Mono<Void> writeWithUdp(Publisher<? extends ByteBuf> body) {
        return this.underlyingOutbound()
                .sendObject(Flux.from(body).map(byteBuf -> new DatagramPacket(byteBuf, remoteAddress)))
                .then();
    }

    public Mono<Void> writeAndFlushWithUdp(Publisher<? extends Publisher<? extends ByteBuf>> publisher) {
        return Flux.from(publisher)
                .flatMap(source -> Flux.from(source).map(byteBuf -> new DatagramPacket(byteBuf, remoteAddress)))
                .flatMap(datagramPacket -> this.underlyingOutbound().sendObject(datagramPacket))
                .then();
    }
}
