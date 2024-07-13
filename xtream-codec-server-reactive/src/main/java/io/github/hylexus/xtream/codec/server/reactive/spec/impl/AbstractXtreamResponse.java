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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.NettyOutbound;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamResponse implements XtreamResponse {
    protected final NettyOutbound delegate;
    protected final ByteBufAllocator byteBufAllocator;

    public AbstractXtreamResponse(NettyOutbound delegate, ByteBufAllocator byteBufAllocator) {
        this.delegate = delegate;
        this.byteBufAllocator = byteBufAllocator;
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
        return this.delegate.send(body).then();
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends ByteBuf>> publisher) {
        return this.delegate.sendGroups(Flux.from(publisher)).then();
    }
}
