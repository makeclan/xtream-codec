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

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.LoggingXtreamRequestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类是从 `org.springframework.web.server.handler.ExceptionHandlingWebHandler` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.server.handler.ExceptionHandlingWebHandler`.
 *
 * @author hylexus
 */
public class ExceptionHandlingXtreamHandler implements XtreamHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingXtreamHandler.class);
    private final XtreamHandler delegateHandler;
    private final List<XtreamRequestExceptionHandler> exceptionHandlers;

    public ExceptionHandlingXtreamHandler(XtreamHandler delegateHandler, List<XtreamRequestExceptionHandler> exceptionHandlers) {
        this.delegateHandler = delegateHandler;

        final List<XtreamRequestExceptionHandler> list = new ArrayList<>();
        if (exceptionHandlers.isEmpty()) {
            log.warn("No [{}] instance configured. Add [{}] as default.",
                    XtreamRequestExceptionHandler.class.getSimpleName(),
                    LoggingXtreamRequestExceptionHandler.class.getSimpleName());
            list.add(new LoggingXtreamRequestExceptionHandler());
        }
        list.add(new XtreamRequestExceptionHandler.CheckpointInsertingXtreamRequestExceptionHandler());
        list.addAll(exceptionHandlers);
        this.exceptionHandlers = OrderedComponent.sort(list);
    }

    @Override
    public Mono<Void> handle(XtreamExchange exchange) {
        Mono<Void> completion;
        try {
            completion = this.delegateHandler.handle(exchange);
        } catch (Throwable ex) {
            completion = Mono.error(ex);
        }

        for (XtreamRequestExceptionHandler handler : this.exceptionHandlers) {
            completion = completion.onErrorResume(ex -> handler.handleRequestException(exchange, ex));
        }

        return completion;
    }
}
