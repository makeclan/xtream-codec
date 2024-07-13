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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;

import java.util.Map;
import java.util.UUID;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamRequest implements XtreamRequest {
    protected final ByteBufAllocator allocator;
    protected final NettyInbound delegate;
    protected final Mono<XtreamSession> sessionMono;
    protected final ByteBuf body;
    protected String id;

    public AbstractXtreamRequest(ByteBufAllocator allocator, NettyInbound delegate, XtreamSession session, ByteBuf body) {
        this.allocator = allocator;
        this.delegate = delegate;
        this.sessionMono = Mono.just(session).cache();
        this.body = body;
    }

    @Override
    public ByteBufAllocator bufferFactory() {
        return this.allocator;
    }

    @Override
    public NettyInbound underlyingInbound() {
        return this.delegate;
    }

    @Override
    public String getId() {
        if (this.id == null) {
            this.id = initId();
        }
        return this.id;
    }

    protected String initId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public ByteBuf payload() {
        return this.body;
    }

    @Override
    public Map<String, Object> attributes() {
        return Map.of();
    }

    @Override
    public Mono<XtreamSession> session() {
        return this.sessionMono;
    }
}
