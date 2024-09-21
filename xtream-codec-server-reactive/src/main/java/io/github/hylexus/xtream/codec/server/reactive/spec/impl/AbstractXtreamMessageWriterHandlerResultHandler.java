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
import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamMessageWriter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

/**
 * @author hylexus
 * @see EntityCodec
 */
public abstract class AbstractXtreamMessageWriterHandlerResultHandler implements XtreamHandlerResultHandler {

    protected final List<XtreamMessageWriter> writers;

    public AbstractXtreamMessageWriterHandlerResultHandler(List<XtreamMessageWriter> writers) {
        this.writers = writers;
    }

    @Override
    public Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult result) {
        if (result.getReturnValue() == null) {
            return Mono.empty();
        }
        for (final XtreamMessageWriter writer : this.writers) {
            if (writer.canWrite(result.getReturnType())) {
                return writer.write(result.getReturnType(), exchange.request(), exchange.response(), result.getReturnValue(), new HashMap<>());
            }
        }
        return Mono.error(new IllegalStateException("No suitable XtreamMessageWriter found for type " + result.getReturnType()));
    }

    @Override
    public int order() {
        return OrderedComponent.BUILTIN_COMPONENT_PRECEDENCE;
    }
}
