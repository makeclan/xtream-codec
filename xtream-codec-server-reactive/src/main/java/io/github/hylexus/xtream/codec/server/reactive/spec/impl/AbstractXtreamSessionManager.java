package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionManager;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractXtreamSessionManager<S extends XtreamSession> implements XtreamSessionManager<S> {
    protected final XtreamSessionIdGenerator sessionIdGenerator;
    protected final Map<String, S> sessions = new ConcurrentHashMap<>();

    public AbstractXtreamSessionManager(XtreamSessionIdGenerator idGenerator) {
        this.sessionIdGenerator = idGenerator;
    }

    @Override
    public Mono<S> getSession(XtreamExchange exchange) {
        return Mono.defer(() -> {
            final String sessionId = this.sessionIdGenerator.generateSessionId(exchange);
            final S session = this.sessions.get(sessionId);
            return Mono.justOrEmpty(session);
        });
    }

    @Override
    public Mono<S> createSession(XtreamExchange exchange) {
        final String sessionId = this.sessionIdGenerator.generateSessionId(exchange);
        final S session = this.doCreateSession(exchange);
        this.sessions.put(sessionId, session);
        return Mono.just(session);
    }

    protected abstract S doCreateSession(XtreamExchange exchange);
}
