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

package io.github.hylexus.xtream.debug.ext.jt808.controller;

import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author hylexus
 */
@RestController
@RequestMapping("/api/v1/message/event")
public class DemoJt808EventController {

    private static final Logger log = LoggerFactory.getLogger(DemoJt808EventController.class);
    private final XtreamEventPublisher eventPublisher;

    public DemoJt808EventController(XtreamEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events">https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events</a>
     */
    @PostMapping("/sse")
    public Flux<ServerSentEvent<Object>> publish() {
        return this.eventPublisher.subscribe().map(event -> {
            // ...
            return ServerSentEvent.builder()
                    .event(String.valueOf(event.type().code()))
                    .data(event.payload())
                    .comment(event.type().description())
                    .build();
        });
    }
}
