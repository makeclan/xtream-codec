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
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class DefaultXtreamExchange implements XtreamExchange {
    protected final XtreamRequest request;
    protected final XtreamResponse response;
    protected final Mono<XtreamSession> sessionMono;

    public DefaultXtreamExchange(XtreamRequest request, XtreamResponse response, XtreamSession session) {
        this(request, response, Mono.just(session).cache());
    }

    public DefaultXtreamExchange(XtreamRequest request, XtreamResponse response, Mono<XtreamSession> session) {
        this.request = request;
        this.response = response;
        this.sessionMono = session;
    }

    @Override
    public XtreamRequest request() {
        return this.request;
    }

    @Override
    public XtreamResponse response() {
        return this.response;
    }

    @Override
    public Mono<XtreamSession> session() {
        return this.sessionMono;
    }

    @Override
    public XtreamExchangeBuilder mutate() {
        return new DefaultXtreamExchangeBuilder(this);
    }


    @Override
    public Map<String, Object> attributes() {
        return new HashMap<>();
    }
}
