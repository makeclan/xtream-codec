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

package io.github.hylexus.xtream.codec.server.reactive.spec.event.builtin;

import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEvent;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventSubscriberInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public class DefaultXtreamEventPublisher implements XtreamEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(DefaultXtreamEventPublisher.class);
    private final ConcurrentMap<String, XtreamEventSubscriberInfo> subscribers = new ConcurrentHashMap<>();
    private final Sinks.Many<XtreamEvent> sink;

    public DefaultXtreamEventPublisher() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @Override
    public void publish(XtreamEvent event) {
        final Sinks.EmitResult emitResult = this.sink.tryEmitNext(event);
        if (emitResult.isFailure()) {
            log.error("Failed to publish event {} with result {}", event, emitResult);
        }
    }

    @Override
    public Predicate<XtreamEvent.XtreamEventType> eventPredicate() {
        // todo 增强判断逻辑
        return eventType -> !this.subscribers.isEmpty();
    }

    @Override
    public Flux<XtreamEvent> subscribe(String id, String description) {
        try {
            final XtreamEventSubscriberInfo info = new XtreamEventSubscriberInfo.DefaultXtreamEventSubscriberInfo(id, description);
            final XtreamEventSubscriberInfo old = this.subscribers.putIfAbsent(id, info);
            if (old != null) {
                return Flux.error(new IllegalStateException("Subscriber already exists: " + old));
            }
            this.subscribers.put(id, info);
            return sink.asFlux()
                    .publishOn(Schedulers.newBoundedElastic(1, 1, "haha"))
                    .doOnError(error -> {
                        // ...
                        log.error("Error occurred", error);
                    })
                    .doOnCancel(() -> {
                        // ...
                        this.subscribers.remove(id);
                    });
        } catch (Throwable e) {
            this.subscribers.remove(id);
            throw e;
        }
    }

    @Override
    public void shutdown() {
        this.sink.tryEmitComplete();
    }

    @Override
    public Stream<XtreamEventSubscriberInfo> subscriberView() {
        return this.subscribers.values().stream();
    }
}
