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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808CommandSender;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 */
public class DefaultJt808XtreamCommandSender implements Jt808CommandSender {
    private static final Logger log = LoggerFactory.getLogger(DefaultJt808XtreamCommandSender.class);
    protected final ByteBufAllocator bufferFactory;
    protected final XtreamSessionManager<Jt808Session> sessionManager;
    protected final Jt808ResponseEncoder jt808ResponseEncoder;

    public DefaultJt808XtreamCommandSender(ByteBufAllocator bufferFactory, XtreamSessionManager<Jt808Session> sessionManager, Jt808ResponseEncoder jt808ResponseEncoder) {
        this.bufferFactory = bufferFactory;
        this.sessionManager = sessionManager;
        this.jt808ResponseEncoder = jt808ResponseEncoder;
    }

    @Override
    public ByteBufAllocator bufferFactory() {
        return this.bufferFactory;
    }

    @Override
    public XtreamSessionManager<Jt808Session> sessionManager() {
        return this.sessionManager;
    }

    @Override
    public Mono<Void> sendByteBuf(String sessionId, Publisher<? extends ByteBuf> data) {
        return this.getSessionById(sessionId).flatMap(session -> {
            return session.writeWith(data);
        });
    }

    @Override
    public Mono<Void> sendObject(String sessionId, Mono<Object> bodyMono) {
        return this.getSessionById(sessionId).flatMap(session -> {
            final String terminalId = session.terminalId();
            final Jt808ProtocolVersion version = session.protocolVersion();
            return bodyMono.flatMap(body -> {
                final Mono<ByteBuf> buffer = this.encodeJt808Message(version, terminalId, body);
                return session.writeWith(buffer);
            });
        });
    }

    protected Mono<ByteBuf> encodeJt808Message(Jt808ProtocolVersion version, String terminalId, Object body) {
        final Jt808ResponseBody annotation = AnnotatedElementUtils.getMergedAnnotation(body.getClass(), Jt808ResponseBody.class);
        if (annotation == null) {
            return Mono.error(new IllegalArgumentException("No @Jt808ResponseBody annotation found on class: " + body.getClass().getName()));
        }
        final ByteBuf buffer = this.jt808ResponseEncoder.encode(body, version, terminalId, annotation);
        log.info("command sender ::: {}", FormatUtils.toHexString(buffer));
        return Mono.just(buffer);
    }

    protected Mono<Jt808Session> getSessionById(String sessionId) {
        return this.sessionManager.getSessionById(sessionId).switchIfEmpty(Mono.defer(() -> {
            // ...
            return Mono.error(new IllegalArgumentException("No session found for sessionId: " + sessionId));
        }));
    }

}
