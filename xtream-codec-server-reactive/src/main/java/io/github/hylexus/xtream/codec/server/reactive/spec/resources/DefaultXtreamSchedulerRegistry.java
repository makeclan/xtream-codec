/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
    protected final ConcurrentHashMap<String, Scheduler> schedulerMap = new ConcurrentHashMap<>();

    public DefaultXtreamSchedulerRegistry(Scheduler defaultNonBlockingScheduler, Scheduler defaultBlockingScheduler) {
        this.defaultNonBlockingScheduler = defaultNonBlockingScheduler;
        this.defaultBlockingScheduler = defaultBlockingScheduler;
        this.registerScheduler(SCHEDULER_NAME_BLOCKING, defaultBlockingScheduler);
        this.registerScheduler(SCHEDULER_NAME_NON_BLOCKING, defaultNonBlockingScheduler);
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
        if (SCHEDULER_NAME_BLOCKING.equals(name) || SCHEDULER_NAME_NON_BLOCKING.equals(name)) {
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
