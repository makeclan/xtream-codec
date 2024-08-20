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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.handler;

import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Jt808ResponseBodyHandlerResultHandler implements XtreamHandlerResultHandler {

    protected final DefaultJt808ResponseEncoder jt808ResponseEncoder;

    public Jt808ResponseBodyHandlerResultHandler(DefaultJt808ResponseEncoder jt808ResponseEncoder) {
        this.jt808ResponseEncoder = jt808ResponseEncoder;
    }

    @Override
    public boolean supports(XtreamHandlerResult handlerResult) {
        return handlerResult.getReturnType().hasMethodAnnotation(Jt808ResponseBody.class);
    }

    @Override
    public Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult handlerResult) {
        final Object returnValue = handlerResult.getReturnValue();
        if (returnValue == null) {
            return Mono.empty();
        }
        final Jt808ResponseBody annotation = handlerResult.getReturnType().getMethodAnnotation(Jt808ResponseBody.class);
        final Jt808Request request = (Jt808Request) exchange.request();
        final Jt808RequestHeader requestHeader = request.header();
        return this.adaptReturnValue(returnValue).flatMap(body -> {
            final ByteBuf responseBuf = this.jt808ResponseEncoder.encode(body, requestHeader.version(), requestHeader.terminalId(), annotation);
            return exchange.response().writeWith(Mono.just(responseBuf));
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
