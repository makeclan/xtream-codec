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

package io.github.hylexus.xtream.codec.ext.jt808.handler;

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
