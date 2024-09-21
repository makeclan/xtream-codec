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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.wirter;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamMessageWriter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamOutbound;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author hylexus
 */
public class Jt808ResponseBodyMessageWriter implements XtreamMessageWriter {
    protected final Jt808ResponseEncoder jt808ResponseEncoder;
    protected final Jt808RequestLifecycleListener lifecycleListener;

    public Jt808ResponseBodyMessageWriter(Jt808ResponseEncoder jt808ResponseEncoder, Jt808RequestLifecycleListener lifecycleListener) {
        this.jt808ResponseEncoder = jt808ResponseEncoder;
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public boolean canWrite(XtreamMethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(Jt808ResponseBody.class);
    }

    @Override
    public Mono<Void> write(XtreamMethodParameter methodParameter, XtreamInbound inbound, XtreamOutbound outbound, Object returnValue, Map<String, Object> context) {
        final Jt808ResponseBody annotation = methodParameter.getMethodAnnotation(Jt808ResponseBody.class);
        final Jt808Request jt808Request = (Jt808Request) inbound;
        final Jt808RequestHeader requestHeader = jt808Request.header();
        return this.adaptReturnValue(returnValue).flatMap(body -> {
            final ByteBuf responseBuf = this.jt808ResponseEncoder.encode(body, requestHeader.version(), requestHeader.terminalId(), annotation);
            this.lifecycleListener.beforeResponseSend(jt808Request, responseBuf);
            return outbound.writeWith(Mono.just(responseBuf));
        });
    }

    protected Mono<?> adaptReturnValue(Object returnValue) {
        return switch (returnValue) {
            case null -> Mono.error(new IllegalArgumentException("message is null"));
            case Mono<?> mono -> mono;
            case Flux<?> ignored -> Mono.error(() -> new IllegalArgumentException("Flux is not supported"));
            case Object object -> Mono.just(object);
        };
    }
}
