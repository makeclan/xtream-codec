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

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpXtreamExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * 当前类是从 `org.springframework.web.server.WebExceptionHandler` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.server.WebExceptionHandler`.
 *
 * @author hylexus
 */
public interface XtreamRequestExceptionHandler extends OrderedComponent {


    Mono<Void> handleRequestException(XtreamExchange exchange, Throwable ex);

    class CheckpointInsertingXtreamRequestExceptionHandler implements XtreamRequestExceptionHandler {

        public CheckpointInsertingXtreamRequestExceptionHandler() {
        }

        @Override
        public Mono<Void> handleRequestException(XtreamExchange exchange, Throwable ex) {
            final String type = exchange instanceof UdpXtreamExchange ? "UDP" : "TCP";
            final String description = "[ExceptionHandlingXtreamHandler] " + "[" + type + "] " + exchange;
            return Mono.<Void>error(ex).checkpoint(description);
        }

        @Override
        public int order() {
            return OrderedComponent.HIGHEST_PRECEDENCE + 1;
        }

    }

    class LoggingXtreamRequestExceptionHandler implements XtreamRequestExceptionHandler {

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
}
