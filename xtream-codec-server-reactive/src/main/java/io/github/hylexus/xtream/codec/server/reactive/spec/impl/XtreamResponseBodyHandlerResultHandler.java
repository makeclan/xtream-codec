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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.annotation.XtreamResponseBody;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamMessageWriter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hylexus
 * @see EntityCodec
 */
public class XtreamResponseBodyHandlerResultHandler extends AbstractXtreamMessageWriterHandlerResultHandler {


    public XtreamResponseBodyHandlerResultHandler(List<XtreamMessageWriter> writers) {
        super(writers);
    }

    @Override
    public boolean supports(XtreamHandlerResult result) {
        return result.getReturnType().getContainerClass().isAnnotationPresent(XtreamResponseBody.class)
                || result.getReturnType().hasMethodAnnotation(XtreamResponseBody.class);
    }

    @Override
    public Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult result) {
        return super.handleResult(exchange, result);
    }

}
