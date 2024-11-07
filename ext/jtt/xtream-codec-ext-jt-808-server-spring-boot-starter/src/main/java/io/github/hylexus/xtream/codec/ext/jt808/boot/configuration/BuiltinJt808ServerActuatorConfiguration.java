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

import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.Jt808MessageDescriptor;
import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.Jt808ServerSimpleMetricsEndpoint;
import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.handler.RequestInfoCollector;
import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.handler.SessionInfoCollector;
import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.values.Jt808ServerSimpleMetricsHolder;
import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.values.SimpleTypes;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.BuiltinJt808DashboardEventController;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.BuiltinJt808DashboardMetricsController;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.handler.Jt808DashboardRequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.concurrent.atomic.LongAdder;

// todo 配置开关

@ConditionalOnAvailableEndpoint(endpoint = Jt808ServerSimpleMetricsEndpoint.class)
public class BuiltinJt808ServerActuatorConfiguration {

    @Bean
    Jt808ServerSimpleMetricsHolder metricsHolder() {
        return new Jt808ServerSimpleMetricsHolder(
                new SimpleTypes.SessionInfo(),
                new SimpleTypes.SessionInfo(),
                new SimpleTypes.SessionInfo(),
                new SimpleTypes.SessionInfo(),
                new SimpleTypes.RequestInfo(new LongAdder(), new HashMap<>()),
                new SimpleTypes.RequestInfo(new LongAdder(), new HashMap<>()),
                new SimpleTypes.RequestInfo(new LongAdder(), new HashMap<>()),
                new SimpleTypes.RequestInfo(new LongAdder(), new HashMap<>())
        );
    }

    @Bean
    @ConditionalOnBean(Jt808SessionManager.class)
    SessionInfoCollector sessionManager(Jt808ServerSimpleMetricsHolder serverSimpleMetricsHolder, Jt808SessionManager sessionManager) {
        return new SessionInfoCollector(serverSimpleMetricsHolder, sessionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808MessageDescriptor jt808MessageDescriptor() {
        return new Jt808MessageDescriptor.Default();
    }

    @Bean
    RequestInfoCollector requestInfoCollector(Jt808ServerSimpleMetricsHolder metricsHolder, Jt808MessageDescriptor descriptor) {
        return new RequestInfoCollector(metricsHolder, descriptor);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 100)
    public Jt808ServerSimpleMetricsEndpoint jt808ServerInfoContributor(Jt808ServerSimpleMetricsHolder metricsHolder) {
        return new Jt808ServerSimpleMetricsEndpoint(metricsHolder);
    }

    @Bean
    BuiltinJt808DashboardEventController builtinJt808DashboardEventController(XtreamEventPublisher eventPublisher) {
        return new BuiltinJt808DashboardEventController(eventPublisher);
    }

    @Bean
    BuiltinJt808DashboardMetricsController builtinJt808DashboardMetricsController(
            XtreamEventPublisher eventPublisher,
            Jt808ServerSimpleMetricsHolder metricsHolder) {
        return new BuiltinJt808DashboardMetricsController(eventPublisher, metricsHolder);
    }

    @Bean
    Jt808DashboardRequestLifecycleListener jt808DashboardRequestLifecycleListener(XtreamEventPublisher eventPublisher) {
        return new Jt808DashboardRequestLifecycleListener(eventPublisher);
    }
}
