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

package io.github.hylexus.xtream.codec.ext.jt808.boot.properties;

import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.BoundedElasticProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.ParallelProperties;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SchedulerType;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SingleProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ToString
public class XtreamServerSchedulerProperties {

    private SchedulerType type = SchedulerType.BOUNDED_ELASTIC;

    @NestedConfigurationProperty
    private BoundedElasticProperties boundedElastic = new BoundedElasticProperties();

    @NestedConfigurationProperty
    private ParallelProperties parallel = new ParallelProperties();

    @NestedConfigurationProperty
    private SingleProperties single = new SingleProperties();

}
