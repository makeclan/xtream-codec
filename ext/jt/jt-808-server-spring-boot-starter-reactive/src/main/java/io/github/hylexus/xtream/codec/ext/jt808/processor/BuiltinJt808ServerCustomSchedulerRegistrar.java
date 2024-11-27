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

package io.github.hylexus.xtream.codec.ext.jt808.processor;

import io.github.hylexus.xtream.codec.ext.jt808.boot.configuration.BuiltinJt808ServerSchedulerConfiguration;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.scheduler.Scheduler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class BuiltinJt808ServerCustomSchedulerRegistrar
        implements PriorityOrdered,
        BeanDefinitionRegistryPostProcessor,
        EnvironmentAware,
        ApplicationContextAware,
        CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BuiltinJt808ServerCustomSchedulerRegistrar.class);
    private Environment environment;
    private ApplicationContext applicationContext;
    private final Map<String, Scheduler> schedulerHolder = new LinkedHashMap<>();

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        final Map<String, XtreamJt808ServerProperties.CustomXtreamServerSchedulerProperties> customSchedulers = getCustomSchedulerConfig();
        if (CollectionUtils.isEmpty(customSchedulers)) {
            return;
        }
        for (final Map.Entry<String, XtreamJt808ServerProperties.CustomXtreamServerSchedulerProperties> entry : customSchedulers.entrySet()) {
            final String name = entry.getKey();
            final XtreamJt808ServerProperties.CustomXtreamServerSchedulerProperties config = entry.getValue();
            if (!StringUtils.hasText(config.getName())) {
                config.setName(name);
            }
            log.info("Registering custom scheduler: {}", config.getName());
            this.registerCustomScheduler(registry, config);
        }
    }

    private void registerCustomScheduler(BeanDefinitionRegistry registry, XtreamJt808ServerProperties.CustomXtreamServerSchedulerProperties config) {
        final Scheduler scheduler = BuiltinJt808ServerSchedulerConfiguration.createScheduler(config);
        final BeanDefinition beanDefinition = BeanDefinitionBuilder
                .rootBeanDefinition(Scheduler.class, () -> scheduler)
                .setPrimary(false)
                .getBeanDefinition();
        registry.registerBeanDefinition(config.getName(), beanDefinition);
        this.schedulerHolder.put(config.getName(), scheduler);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return -1024;
    }

    @Override
    public void run(String... args) {
        // todo 换一个更好的实现方式
        final XtreamSchedulerRegistry schedulerRegistry = this.applicationContext.getBean(XtreamSchedulerRegistry.class);
        for (final Map.Entry<String, Scheduler> entry : this.schedulerHolder.entrySet()) {
            final String name = entry.getKey();
            final Scheduler scheduler = entry.getValue();
            schedulerRegistry.registerScheduler(name, scheduler);
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<String, XtreamJt808ServerProperties.CustomXtreamServerSchedulerProperties> getCustomSchedulerConfig() {
        final XtreamJt808ServerProperties.SchedulerProperties schedulerProperties = Binder.get(this.environment)
                // Map<String, CustomXtreamServerSchedulerProperties> 不方便绑定
                // 直接绑定到上一级
                .bind("jt808-server.schedulers", XtreamJt808ServerProperties.SchedulerProperties.class).get();
        return schedulerProperties.getCustomSchedulers();
    }
}
