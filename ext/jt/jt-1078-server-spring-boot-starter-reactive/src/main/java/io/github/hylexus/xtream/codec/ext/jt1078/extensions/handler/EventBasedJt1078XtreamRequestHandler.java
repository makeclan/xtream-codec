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

package io.github.hylexus.xtream.codec.ext.jt1078.extensions.handler;

import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078RequestPublisher;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.SimpleXtreamRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class EventBasedJt1078XtreamRequestHandler implements SimpleXtreamRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(EventBasedJt1078XtreamRequestHandler.class);
    private final Jt1078RequestPublisher requestPublisher;

    public EventBasedJt1078XtreamRequestHandler(Jt1078RequestPublisher requestPublisher) {
        this.requestPublisher = requestPublisher;
    }

    @Override
    public Mono<Void> handle(XtreamExchange exchange) {
        final Jt1078Request request = exchange.request().castAs(Jt1078Request.class);
        return requestPublisher.publish(request);
    }

}
