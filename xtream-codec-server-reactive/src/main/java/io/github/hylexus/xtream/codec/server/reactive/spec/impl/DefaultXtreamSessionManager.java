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
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionManager;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultXtreamSessionManager implements XtreamSessionManager {
    protected final XtreamSessionIdGenerator sessionIdGenerator = new XtreamSessionIdGenerator.DefalutXtreamSessionIdGenerator();
    protected final Map<String, XtreamSession> sessions = new ConcurrentHashMap<>();

    public DefaultXtreamSessionManager() {
    }

    @Override
    public Mono<XtreamSession> getSession(XtreamExchange exchange) {
        return Mono.defer(() -> {
            final String sessionId = this.sessionIdGenerator.generateSessionId(exchange);
            final XtreamSession session = this.sessions.get(sessionId);
            if (session == null) {
                return Mono.empty();
            }
            return Mono.just(session);
        });
    }

    @Override
    public Mono<XtreamSession> createSession(XtreamExchange exchange) {
        final String sessionId = this.sessionIdGenerator.generateSessionId(exchange);
        final XtreamSession session = new DefaultXtreamSession(sessionId, exchange.request().type());
        this.sessions.put(sessionId, session);
        return Mono.just(session);
    }

}
