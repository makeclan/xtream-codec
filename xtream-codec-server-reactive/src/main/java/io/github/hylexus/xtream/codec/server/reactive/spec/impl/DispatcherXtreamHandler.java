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


import io.github.hylexus.xtream.codec.common.exception.NotYetImplementedException;
import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 当前类是从 `org.springframework.web.reactive.DispatcherHandler` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.reactive.DispatcherHandler`.
 *
 * @author hylexus
 */
public class DispatcherXtreamHandler implements XtreamHandler {

    private final List<XtreamHandlerMapping> handlerMappings;

    private final List<XtreamHandlerAdapter> handlerAdapters;

    private final List<XtreamHandlerResultHandler> resultHandlers;

    public DispatcherXtreamHandler(List<XtreamHandlerMapping> handlerMappings, List<XtreamHandlerAdapter> handlerAdapters, List<XtreamHandlerResultHandler> resultHandlers) {
        this.handlerMappings = OrderedComponent.sort(handlerMappings);
        this.handlerAdapters = OrderedComponent.sort(handlerAdapters);
        this.resultHandlers = OrderedComponent.sort(resultHandlers);
    }

    @Override
    public Mono<Void> handle(XtreamExchange exchange) {
        return Flux.fromIterable(this.handlerMappings)
                .concatMap(mapping -> mapping.getHandler(exchange))
                .next()
                .switchIfEmpty(createNotFoundError())
                .onErrorResume(ex -> handleResultMono(exchange, Mono.error(ex)))
                .flatMap(handler -> handleRequestWith(exchange, handler));
    }

    private Mono<Void> handleRequestWith(XtreamExchange exchange, Object handler) {

        if (this.handlerAdapters != null) {
            for (XtreamHandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    Mono<XtreamHandlerResult> resultMono = adapter.handle(exchange, handler);
                    return handleResultMono(exchange, resultMono);
                }
            }
        }
        return Mono.error(new IllegalStateException("No HandlerAdapter: " + handler));
    }

    private Mono<Void> handleResultMono(XtreamExchange exchange, Mono<XtreamHandlerResult> resultMono) {
        if (this.handlerAdapters != null) {
            for (XtreamHandlerAdapter adapter : this.handlerAdapters) {
                if (adapter instanceof XtreamDispatchExceptionHandler exceptionHandler) {
                    resultMono = resultMono.onErrorResume(ex2 -> exceptionHandler.handleError(exchange, ex2));
                }
            }
        }
        return resultMono.flatMap(result -> {
            Mono<Void> voidMono = handleResult(exchange, result, "Handler " + result.getHandler());
            if (result.getExceptionHandler() != null) {
                voidMono = voidMono.onErrorResume(ex ->
                        result.getExceptionHandler().handleError(exchange, ex).flatMap(result2 ->
                                handleResult(exchange, result2, "Exception handler "
                                        + result2.getHandler() + ", error=\"" + ex.getMessage() + "\"")));
            }
            return voidMono;
        });
    }

    private Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult handlerResult, String description) {

        if (this.resultHandlers != null) {
            for (XtreamHandlerResultHandler resultHandler : this.resultHandlers) {
                if (resultHandler.supports(handlerResult)) {
                    description += " [DispatcherHandler]";
                    return resultHandler.handleResult(exchange, handlerResult).checkpoint(description);
                }
            }
        }
        return Mono.error(new IllegalStateException(
                "No HandlerResultHandler for " + handlerResult.getReturnValue()));
    }

    private <R> Mono<R> createNotFoundError() {
        return Mono.defer(() -> {
            Exception ex = new NotYetImplementedException("createNotFoundError");
            return Mono.error(ex);
        });
    }

}
