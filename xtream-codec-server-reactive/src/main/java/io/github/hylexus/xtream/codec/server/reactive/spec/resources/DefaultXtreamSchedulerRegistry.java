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

package io.github.hylexus.xtream.codec.server.reactive.spec.resources;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import reactor.core.scheduler.Scheduler;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
public class DefaultXtreamSchedulerRegistry implements XtreamSchedulerRegistry {

    protected final Scheduler defaultNonBlockingScheduler;
    protected final Scheduler defaultBlockingScheduler;
    protected final Scheduler eventPublisherScheduler;
    protected final ConcurrentHashMap<String, Scheduler> schedulerMap = new ConcurrentHashMap<>();

    public DefaultXtreamSchedulerRegistry(Scheduler defaultNonBlockingScheduler, Scheduler defaultBlockingScheduler, Scheduler eventPublisherScheduler) {
        this.defaultNonBlockingScheduler = defaultNonBlockingScheduler;
        this.defaultBlockingScheduler = defaultBlockingScheduler;
        this.eventPublisherScheduler = eventPublisherScheduler;
        this.registerScheduler(SCHEDULER_NAME_BLOCKING, defaultBlockingScheduler);
        this.registerScheduler(SCHEDULER_NAME_NON_BLOCKING, defaultNonBlockingScheduler);
        this.registerScheduler(SCHEDULER_NAME_EVENT_PUBLISHER, eventPublisherScheduler);
    }

    @Override
    public Scheduler defaultNonBlockingScheduler() {
        return this.defaultNonBlockingScheduler;
    }

    @Override
    public Scheduler defaultBlockingScheduler() {
        return this.defaultBlockingScheduler;
    }

    @Override
    public Scheduler eventPublisherScheduler() {
        return this.eventPublisherScheduler;
    }

    @Override
    public Optional<Scheduler> getScheduler(String name) {
        return Optional.ofNullable(this.schedulerMap.get(name));
    }

    @Override
    public boolean registerScheduler(String name, Scheduler scheduler) {
        final Scheduler v = this.schedulerMap.putIfAbsent(name, scheduler);
        return v == null;
    }

    @Override
    public boolean removeScheduler(String name) {
        if (SCHEDULER_NAME_BLOCKING.equals(name) || SCHEDULER_NAME_NON_BLOCKING.equals(name) || SCHEDULER_NAME_EVENT_PUBLISHER.equals(name)) {
            throw new UnsupportedOperationException("Cannot remove default scheduler");
        }

        final Scheduler v = this.schedulerMap.remove(name);
        return v != null;
    }

    @Override
    public Map<String, Scheduler> asMapView() {
        return Collections.unmodifiableMap(this.schedulerMap);
    }

}
