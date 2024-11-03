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
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamIntervalChecker;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.SessionIdleStateCheckerProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamSessionManager<S extends XtreamSession> implements XtreamSessionManager<S> {
    private static final Logger log = LoggerFactory.getLogger(AbstractXtreamSessionManager.class);
    protected final XtreamSessionIdGenerator sessionIdGenerator;
    protected final ConcurrentMap<String, S> sessions = new ConcurrentHashMap<>();
    protected final XtreamIntervalChecker checker;
    private final Duration maxIdleTime;
    private final Clock clock = Clock.system(ZoneId.of("Asia/Shanghai"));
    private final Lock lock = new ReentrantLock();

    public AbstractXtreamSessionManager(XtreamSessionIdGenerator idGenerator, SessionIdleStateCheckerProps checkerProps) {
        this.sessionIdGenerator = idGenerator;
        if (checkerProps.isEnabled()) {
            this.checker = new XtreamIntervalChecker(
                    "session-checker",
                    checkerProps.getCheckInterval(),
                    checkerProps.getCheckBackoffTime(),
                    (name, interval) -> {
                        log.debug("{} interval: {}", name, interval);
                        this.doCheckStatus();
                        return Mono.empty();
                    }
            );
            this.checker.start();
        } else {
            this.checker = null;
        }
        this.maxIdleTime = checkerProps.getMaxIdleTime();
    }

    void doCheckStatus() {
        if (sessions.isEmpty()) {
            return;
        }

        // 加锁失败就等下一次
        if (this.lock.tryLock()) {
            try {
                final Iterator<S> iterator = this.sessions.values().iterator();
                final Instant now = this.clock.instant();
                while (iterator.hasNext()) {
                    final S session = iterator.next();
                    if (isExpired(session, now)) {
                        log.info("Session expired, remove it: {}", session);
                        session.invalidate();
                    }
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    protected boolean isExpired(S session, Instant now) {
        return now.minus(this.maxIdleTime).isAfter(session.lastCommunicateTime());
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

    @Override
    public Mono<S> getSessionById(String sessionId) {
        final S s = this.sessions.get(sessionId);
        return Mono.justOrEmpty(s);
    }

    @Override
    public Stream<S> list() {
        return this.sessions.values().stream();
    }

    @Override
    public long count() {
        return this.sessions.size();
    }

    @Override
    public void shutdown() {
        if (this.checker != null) {
            this.checker.stop();
        }
    }
}
