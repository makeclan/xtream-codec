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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamMessageWriter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamMessageWriterHandlerResultHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hylexus
 */
public class Jt808ResponseBodyHandlerResultHandler extends AbstractXtreamMessageWriterHandlerResultHandler {

    public Jt808ResponseBodyHandlerResultHandler(List<XtreamMessageWriter> writers) {
        super(writers);
    }

    @Override
    public boolean supports(XtreamHandlerResult handlerResult) {
        return handlerResult.getReturnType().hasMethodAnnotation(Jt808ResponseBody.class);
    }

    @Override
    public Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult result) {
        return super.handleResult(exchange, result);
    }

    @Override
    public int order() {
        return super.order() - 100;
    }
}
