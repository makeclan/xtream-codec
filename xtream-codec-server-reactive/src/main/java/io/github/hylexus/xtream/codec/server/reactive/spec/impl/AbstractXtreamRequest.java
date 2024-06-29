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
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;

import java.util.Map;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamRequest implements XtreamRequest {

    protected final NettyInbound delegate;
    protected final Mono<XtreamSession> sessionMono;
    protected final ByteBuf body;

    public AbstractXtreamRequest(NettyInbound delegate, XtreamSession session, ByteBuf body) {
        this.delegate = delegate;
        this.sessionMono = Mono.just(session).cache();
        this.body = body;
    }

    @Override
    public NettyInbound underlyingInbound() {
        return this.delegate;
    }

    @Override
    public ByteBuf body() {
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
