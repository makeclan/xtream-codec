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

import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto.LinkDataDto;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.events.Jt808DashboardEventPayloads;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.events.Jt808DashboardEventType;
import io.github.hylexus.xtream.codec.ext.jt808.exception.BadRequestException;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtWebUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEvent;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventSubscriberInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@RestController
@RequestMapping("/dashboard-api/v1/event")
public class BuiltinJt808DashboardEventController {
    private static final Logger log = LoggerFactory.getLogger(BuiltinJt808DashboardEventController.class);
    private final XtreamEventPublisher eventPublisher;

    public BuiltinJt808DashboardEventController(XtreamEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events">https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events</a>
     */
    @GetMapping("/link-data")
    public Flux<ServerSentEvent<Object>> getLinkData(ServerWebExchange exchange, @Validated LinkDataDto dto) {
        return this.createServerSentEventFlux(exchange, dto);
    }

    @PostMapping("/link-data")
    public Flux<ServerSentEvent<Object>> linkData(ServerWebExchange exchange, @Validated @RequestBody LinkDataDto dto) {
        return this.createServerSentEventFlux(exchange, dto);
    }

    private Flux<ServerSentEvent<Object>> createServerSentEventFlux(ServerWebExchange exchange, LinkDataDto dto) {
        final Set<XtreamEvent.XtreamEventType> interestedEvents = this.convertToXtreamEventTypes(dto);

        // final Predicate<XtreamEvent> filter = this.createTypeFilter(interestedEvents).and(this.createTerminalIdFilter(dto));

        final String clientIp = JtWebUtils.getClientIp(exchange.getRequest().getHeaders()::getFirst).orElse("unknown");
        return this.eventPublisher.subscribe(
                        XtreamEventSubscriberInfo.of(
                                interestedEvents,
                                Map.of("clientIp", clientIp, "source", "DashboardApi")
                        )
                )
                // .filter(filter)
                .filter(this.createTerminalIdFilter(dto))
                .map(event -> {
                    // ...
                    return ServerSentEvent.builder()
                            .event(String.valueOf(event.type().code()))
                            .data(event.payload())
                            .comment(event.type().description())
                            .build();
                });
    }

    private Predicate<XtreamEvent> createTerminalIdFilter(LinkDataDto dto) {
        return it -> {
            if (it.payload() instanceof Jt808DashboardEventPayloads.HasTerminalId hasTerminalId) {
                return hasTerminalId.match(dto.getTerminalId());
            }
            return false;
        };
    }

    Set<XtreamEvent.XtreamEventType> convertToXtreamEventTypes(LinkDataDto dto) {
        if (CollectionUtils.isEmpty(dto.getEventCodes())) {
            return Set.of(XtreamEvent.XtreamEventType.ALL);
        }
        return dto.getEventCodes().stream().map(code -> {
            // ...
            return XtreamEvent.DefaultXtreamEventType.of(code)
                    .or(() -> Jt808DashboardEventType.of(code))
                    .orElseThrow(() -> new BadRequestException("Invalid event code: " + code));
        }).collect(Collectors.toSet());
    }

    private Predicate<XtreamEvent> createTypeFilter(Set<XtreamEvent.XtreamEventType> eventTypes) {
        if (eventTypes == null || eventTypes.isEmpty() || eventTypes.contains(XtreamEvent.DefaultXtreamEventType.ALL)) {
            return it -> true;
        }

        return it -> eventTypes.contains(it.type());
    }

}
