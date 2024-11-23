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

import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamServerSchedulerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.BoundedElasticProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.ParallelProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SchedulerType;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SingleProperties;
import io.github.hylexus.xtream.codec.ext.jt808.processor.BuiltinJt808ServerCustomSchedulerRegistrar;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamServerConstants;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.builtin.DefaultXtreamEventPublisher;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.DefaultXtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.DefaultXtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.XtreamReactorThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Import(BuiltinJt808ServerCustomSchedulerRegistrar.class)
public class BuiltinJt808ServerSchedulerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    XtreamBlockingHandlerMethodPredicate xtreamBlockingHandlerMethodPredicate() {
        return new DefaultXtreamBlockingHandlerMethodPredicate();
    }

    @Bean
    @ConditionalOnMissingBean
    XtreamSchedulerRegistry xtreamSchedulerRegistry(
            @Qualifier(XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER) Scheduler nonBlockingScheduler,
            @Qualifier(XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER) Scheduler blockingScheduler,
            @Qualifier(XtreamServerConstants.BEAN_NAME_EVENT_PUBLISHER_SCHEDULER) Scheduler eventPublisherScheduler) {
        return new DefaultXtreamSchedulerRegistry(nonBlockingScheduler, blockingScheduler, eventPublisherScheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    XtreamEventPublisher xtreamEventPublisher(XtreamSchedulerRegistry schedulerRegistry) {
        return new DefaultXtreamEventPublisher(schedulerRegistry);
    }

    @Bean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER)
    @ConditionalOnMissingBean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER)
    Scheduler nonBlockingScheduler(XtreamJt808ServerProperties serverProperties) {
        final XtreamServerSchedulerProperties property = serverProperties.getSchedulers().getNonBlockingHandler();
        return createScheduler(property);
    }

    @Bean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER)
    @ConditionalOnMissingBean(name = XtreamServerConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER)
    Scheduler blockingScheduler(XtreamJt808ServerProperties serverProperties) {
        final XtreamServerSchedulerProperties property = serverProperties.getSchedulers().getBlockingHandler();
        return createScheduler(property);
    }

    @Bean(name = XtreamServerConstants.BEAN_NAME_EVENT_PUBLISHER_SCHEDULER)
    @ConditionalOnMissingBean(name = XtreamServerConstants.BEAN_NAME_EVENT_PUBLISHER_SCHEDULER)
    Scheduler eventPublisherScheduler(XtreamJt808ServerProperties serverProperties) {
        final XtreamServerSchedulerProperties property = serverProperties.getSchedulers().getEventPublisher();
        return createScheduler(property);
    }

    @SuppressWarnings("deprecation")
    public static Scheduler createScheduler(XtreamServerSchedulerProperties property) {
        final SchedulerType schedulerType = property.getType();
        return switch (schedulerType) {
            case BOUNDED_ELASTIC -> newBoundedElastic(property.getBoundedElastic());
            case PARALLEL -> newParallel(property.getParallel());
            case VIRTUAL -> Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor());
            case SINGLE -> newSingle(property.getSingle());
            case IMMEDIATE -> Schedulers.immediate();
            default -> throw new IllegalArgumentException("Unsupported SchedulerType: " + schedulerType);
        };
    }

    private static Scheduler newSingle(SingleProperties single) {
        return Schedulers.newSingle(
                new XtreamReactorThreadFactory(
                        single.getThreadNamePrefix(),
                        single.isDaemon(),
                        single.isRejectBlockingTask()
                )
        );
    }

    private static Scheduler newParallel(ParallelProperties properties) {
        return Schedulers.newParallel(
                properties.getParallelism(),
                new XtreamReactorThreadFactory(
                        properties.getThreadNamePrefix(),
                        properties.isDaemon(),
                        properties.isRejectBlockingTask()
                )
        );
    }

    private static Scheduler newBoundedElastic(BoundedElasticProperties properties) {
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
