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

package io.github.hylexus.xtream.debug.ext.jt808;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.exception.RequestHandlerNotFoundException;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GlobalXtreamServerExceptionHandler implements XtreamRequestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalXtreamServerExceptionHandler.class);

    @Override
    public Mono<Void> handleRequestException(XtreamExchange exchange, @Nonnull Throwable ex) {
        if (ex instanceof RequestHandlerNotFoundException) {
            log.error("receive unknown msg: {}", exchange.request());
            return Mono.empty();
        }
        log.error("Error: ", ex);
        return Mono.empty();
    }

}
