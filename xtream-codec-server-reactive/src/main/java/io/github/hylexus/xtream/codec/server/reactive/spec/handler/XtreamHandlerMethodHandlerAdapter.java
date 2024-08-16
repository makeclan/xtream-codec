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

package io.github.hylexus.xtream.codec.server.reactive.spec.handler;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.ReactiveXtreamHandlerMethod;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamHandlerMethod;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public class XtreamHandlerMethodHandlerAdapter implements XtreamHandlerAdapter {

    protected static final Mono<Object[]> EMPTY_ARGS = Mono.just(new Object[0]);
    protected static final Object NO_ARG_VALUE = new Object();
    protected final XtreamHandlerMethodArgumentResolver argumentResolver;

    protected final Scheduler blockingScheduler;
    protected final Scheduler nonBlockingScheduler;

    public XtreamHandlerMethodHandlerAdapter(XtreamHandlerMethodArgumentResolver argumentResolver, Scheduler nonBlockingScheduler, Scheduler blockingScheduler) {
        this.argumentResolver = argumentResolver;
        this.blockingScheduler = blockingScheduler;
        this.nonBlockingScheduler = nonBlockingScheduler;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof XtreamHandlerMethod;
    }

    @Override
    public Mono<XtreamHandlerResult> handle(XtreamExchange exchange, Object handler) {
        final XtreamHandlerMethod handlerMethod = (XtreamHandlerMethod) handler;
        final Scheduler scheduler = handlerMethod instanceof ReactiveXtreamHandlerMethod
                ? this.nonBlockingScheduler
                : this.blockingScheduler;

        return this.resolveArguments(exchange, handlerMethod).flatMap(args -> {
            // ...
            return handlerMethod.invoke(handlerMethod.getContainerInstance(), args);
        }).subscribeOn(scheduler);
    }

    private Mono<Object[]> resolveArguments(XtreamExchange exchange, XtreamHandlerMethod handlerMethod) {
        final XtreamMethodParameter[] parameters = handlerMethod.getParameters();
        if (parameters.length == 0) {
            return EMPTY_ARGS;
        }

        final List<Mono<Object>> args = new ArrayList<>(parameters.length);
        for (final XtreamMethodParameter parameter : parameters) {
            if (!this.argumentResolver.supportsParameter(parameter)) {
                return Mono.error(new IllegalStateException("Cannot resolve argument: " + parameter));
            }
            final Mono<Object> mono = this.argumentResolver.resolveArgument(parameter, exchange)
                    .defaultIfEmpty(NO_ARG_VALUE);
            args.add(mono);
        }
        return Mono.zip(args, objects -> {
            // ...
            return Stream.of(objects).map(it -> it == NO_ARG_VALUE ? null : it).toArray();
        });
    }
}
