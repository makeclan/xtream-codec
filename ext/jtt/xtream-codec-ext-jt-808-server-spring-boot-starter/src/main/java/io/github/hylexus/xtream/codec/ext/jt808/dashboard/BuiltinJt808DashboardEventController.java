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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard;

import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto.LinkDataDto;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto.LinkDataType;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808DashboardEventPayloads;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808DashboardEventType;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEvent;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author hylexus
 */
@RestController
@RequestMapping("/api/v1/jt808-dashboard/event")
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
    public Flux<ServerSentEvent<Object>> getLinkData(@Validated LinkDataDto dto) {
        return this.createServerSentEventFlux(dto);
    }

    @PostMapping("/link-data")
    public Flux<ServerSentEvent<Object>> linkData(@Validated @RequestBody LinkDataDto dto) {
        return this.createServerSentEventFlux(dto);
    }

    private Flux<ServerSentEvent<Object>> createServerSentEventFlux(LinkDataDto dto) {
        final Predicate<XtreamEvent> filter = this.createTypeFilter(dto).and(this.createTerminalIdFilter(dto));
        return this.eventPublisher.subscribe()
                .filter(filter)
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

    private Predicate<XtreamEvent> createTypeFilter(LinkDataDto dto) {
        if (dto.getType() == null || dto.getType().isEmpty() || dto.getType().contains(LinkDataType.ALL)) {
            return it -> true;
        }
        final Set<XtreamEvent.XtreamEventType> eventTypes = new HashSet<>();
        for (final LinkDataType type : dto.getType()) {
            switch (type) {
                case REQUEST -> eventTypes.add(Jt808DashboardEventType.RECEIVE_PACKAGE);
                case MERGED_REQUEST -> eventTypes.add(Jt808DashboardEventType.MERGE_PACKAGE);
                case RESPONSE -> eventTypes.add(Jt808DashboardEventType.SEND_PACKAGE);
                case COMMAND -> eventTypes.add(Jt808DashboardEventType.COMMAND);
                default -> {
                    // ignore
                }
            }
        }
        return it -> eventTypes.contains(it.type());
    }

}
