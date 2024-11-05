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

import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.Jt808ServerSimpleMetricsHolder;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author hylexus
 */
@RestController
@RequestMapping("/api/v1/")
public class DemoJt808EventController {

    private static final Logger log = LoggerFactory.getLogger(DemoJt808EventController.class);
    private final XtreamEventPublisher eventPublisher;
    private final Jt808ServerSimpleMetricsHolder metricsHolder;

    public DemoJt808EventController(XtreamEventPublisher eventPublisher, Jt808ServerSimpleMetricsHolder metricsHolder) {
        this.eventPublisher = eventPublisher;
        this.metricsHolder = metricsHolder;
    }

    /**
     * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events">https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events</a>
     */
    @GetMapping("/metrics")
    public Flux<ServerSentEvent<Object>> metrics(
            @Validated
            @Min(message = "`duration` must be greater than or equal to 5", value = 5)
            @Max(message = "`duration` must be letter than or equal to 60", value = 60)
            @RequestParam(value = "duration", required = false, defaultValue = "5") long duration) {

        return Flux.just(metricsHolder)
                .concatWith(Flux.interval(Duration.ofSeconds(duration)).map(ignore -> metricsHolder))
                .map(metricsHolder -> ServerSentEvent.<Object>builder(metricsHolder).build());
    }

    /**
     * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events">https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events</a>
     */
    @PostMapping("/event/all")
    public Flux<ServerSentEvent<Object>> allEvents() {
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
