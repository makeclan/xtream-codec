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

package io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin;

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class LoggingXtreamRequestExceptionHandler implements XtreamRequestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(XtreamRequestExceptionHandler.class);

    public LoggingXtreamRequestExceptionHandler() {
    }

    @Override
    public Mono<Void> handleRequestException(XtreamExchange exchange, Throwable ex) {
        log.error("[LoggingXtreamRequestExceptionHandler] ", ex);
        return Mono.empty();
    }

    @Override
    public int order() {
        return OrderedComponent.LOWEST_PRECEDENCE - 1;
    }
}
