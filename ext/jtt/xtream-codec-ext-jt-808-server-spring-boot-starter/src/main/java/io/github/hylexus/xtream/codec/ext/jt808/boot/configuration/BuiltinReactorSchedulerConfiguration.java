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

package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration;

import io.github.hylexus.xtream.codec.ext.jt808.boot.condition.ConditionalOnMissingCustomizedScheduler;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamServerSchedulerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.BoundedElasticProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.ParallelProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SchedulerType;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SingleProperties;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamServerConstants;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.DefaultXtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.DefaultXtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.XtreamReactorThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;


public class BuiltinReactorSchedulerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    XtreamBlockingHandlerMethodPredicate xtreamBlockingHandlerMethodPredicate() {
        return new DefaultXtreamBlockingHandlerMethodPredicate();
    }

    @Bean
    @ConditionalOnMissingBean
    XtreamSchedulerRegistry xtreamSchedulerRegistry(
            @Qualifier(XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER) Scheduler nonBlockingScheduler,
            @Qualifier(XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER) Scheduler blockingScheduler) {
        return new DefaultXtreamSchedulerRegistry(nonBlockingScheduler, blockingScheduler);
    }

    @Bean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER)
    @ConditionalOnMissingBean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER)
    @ConditionalOnMissingCustomizedScheduler(type = ConditionalOnMissingCustomizedScheduler.Type.NON_BLOCKING)
    Scheduler nonBlockingScheduler(XtreamJt808ServerProperties serverProperties) {
        final XtreamServerSchedulerProperties property = serverProperties.getHandlerSchedulers().getNonBlockingScheduler();
        return this.createScheduler(property);
    }

    @Bean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER)
    @ConditionalOnMissingBean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER)
    @ConditionalOnMissingCustomizedScheduler(type = ConditionalOnMissingCustomizedScheduler.Type.BLOCKING)
    Scheduler blockingScheduler(XtreamJt808ServerProperties serverProperties) {
        final XtreamServerSchedulerProperties property = serverProperties.getHandlerSchedulers().getBlockingScheduler();
        return this.createScheduler(property);
    }

    @SuppressWarnings("deprecation")
    private Scheduler createScheduler(XtreamServerSchedulerProperties property) {
        final SchedulerType schedulerType = property.getType();
        return switch (schedulerType) {
            case BOUNDED_ELASTIC -> this.newBoundedElastic(property.getBoundedElastic());
            case PARALLEL -> this.newParallel(property.getParallel());
            case VIRTUAL -> Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor());
            case SINGLE -> this.newSingle(property.getSingle());
            case IMMEDIATE -> Schedulers.immediate();
            default -> throw new IllegalArgumentException("Unsupported SchedulerType: " + schedulerType);
        };
    }

    private Scheduler newSingle(SingleProperties single) {
        return Schedulers.newSingle(
                new XtreamReactorThreadFactory(
                        single.getThreadNamePrefix(),
                        single.isDaemon(),
                        single.isRejectBlockingTask()
                )
        );
    }

    private Scheduler newParallel(ParallelProperties properties) {
        return Schedulers.newParallel(
                properties.getParallelism(),
                new XtreamReactorThreadFactory(
                        properties.getThreadNamePrefix(),
                        properties.isDaemon(),
                        properties.isRejectBlockingTask()
                )
        );
    }

    private Scheduler newBoundedElastic(BoundedElasticProperties properties) {
        return Schedulers.newBoundedElastic(
                properties.getThreadCapacity(),
                properties.getQueuedTaskCapacity(),
                new XtreamReactorThreadFactory(
                        properties.getThreadNamePrefix(),
                        properties.isDaemon(),
                        properties.isRejectBlockingTask()
                ),
                (int) properties.getTtl().toSeconds()
        );
    }
}
