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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.controller;

import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808ServerSimpleMetricsHolder;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo.SimpleMetricsVo;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Supplier;

@RestController
@RequestMapping("/dashboard-api/v1/metrics")
public class BuiltinJt808DashboardMetricsController {
    private static final Logger log = LoggerFactory.getLogger(BuiltinJt808DashboardMetricsController.class);
    private final XtreamEventPublisher eventPublisher;
    private final Jt808ServerSimpleMetricsHolder metricsHolder;

    public BuiltinJt808DashboardMetricsController(XtreamEventPublisher eventPublisher, Jt808ServerSimpleMetricsHolder metricsHolder) {
        this.eventPublisher = eventPublisher;
        this.metricsHolder = metricsHolder;
    }

    /**
     * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events">https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events</a>
     */
    @RequestMapping(value = "/basic", method = {RequestMethod.GET, RequestMethod.POST})
    public Flux<ServerSentEvent<Object>> metrics(
            @Validated
            @Min(message = "`duration` must be greater than or equal to 5", value = 5)
            @Max(message = "`duration` must be letter than or equal to 60", value = 60)
            @RequestParam(value = "duration", required = false, defaultValue = "5") long duration) {

        final Supplier<SimpleMetricsVo> supplier = () -> new SimpleMetricsVo(metricsHolder, this.eventPublisher.subscriberCount());

        return Flux.just(supplier.get())
                .concatWith(Flux.interval(Duration.ofSeconds(duration)).map(ignore -> supplier.get()))
                .map(metricsHolder -> ServerSentEvent.<Object>builder(metricsHolder).build());
    }

}
