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
