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

package io.github.hylexus.xtream.codec.ext.jt808.boot.properties;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.scheduler.BoundedElasticProperties;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.scheduler.ParallelProperties;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.scheduler.SchedulerType;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.scheduler.SingleProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class XtreamServerSchedulerProperties {

    protected SchedulerType type = SchedulerType.BOUNDED_ELASTIC;
    protected boolean metricsEnabled = false;
    protected String metricsPrefix;
    protected Map<String, Serializable> metadata = new LinkedHashMap<>();

    @NestedConfigurationProperty
    protected BoundedElasticProperties boundedElastic = new BoundedElasticProperties();

    @NestedConfigurationProperty
    protected ParallelProperties parallel = new ParallelProperties();

    @NestedConfigurationProperty
    protected SingleProperties single = new SingleProperties();

    public XtreamSchedulerRegistry.SchedulerConfig toSchedulerConfig(String name) {
        return XtreamSchedulerRegistry.SchedulerConfig.newBuilder()
                .name(name)
                .metricsEnabled(this.metricsEnabled)
                .metricsPrefix(this.metricsPrefix)
                .metadata(this.metadata)
                .build();
    }

}
