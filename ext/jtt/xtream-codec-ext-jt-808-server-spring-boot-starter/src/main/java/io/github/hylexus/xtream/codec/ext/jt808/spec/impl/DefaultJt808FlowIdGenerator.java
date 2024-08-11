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

package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808FlowIdGenerator;
import org.assertj.core.util.VisibleForTesting;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 该实现类的灵感来自于 <a href="https://github.com/spring-cloud/spring-cloud-commons/blob/8ec45e03b00f3ad96bbc03a86390ed029364bb89/spring-cloud-loadbalancer/src/main/java/org/springframework/cloud/loadbalancer/core/RoundRobinLoadBalancer.java#L111">RoundRobinLoadBalancer</a>
 * <ol>
 *     <li>去掉了 `synchronized`</li>
 *     <li>使用 {@link #mask(int)} 来处理溢出的情况</li>
 * </ol>
 *
 * @author hylexus
 * @see <a href="https://github.com/spring-cloud/spring-cloud-commons/blob/8ec45e03b00f3ad96bbc03a86390ed029364bb89/spring-cloud-loadbalancer/src/main/java/org/springframework/cloud/loadbalancer/core/RoundRobinLoadBalancer.java#L111">RoundRobinLoadBalancer</a>
 */
public class DefaultJt808FlowIdGenerator implements Jt808FlowIdGenerator {

    private final AtomicInteger currentValue;

    public DefaultJt808FlowIdGenerator() {
        this.currentValue = new AtomicInteger(0);
    }

    @VisibleForTesting
    DefaultJt808FlowIdGenerator(int init) {
        this.currentValue = new AtomicInteger(this.mask(init));
    }

    @Override
    public int flowId(int increment) {
        final int next = this.currentValue.addAndGet(increment);
        return this.mask(next);
    }

    @Override
    public int[] flowIds(int count) {
        int last = this.flowId(count) - 1;
        final int[] ids = new int[count];
        for (int i = count - 1; i >= 0; i--) {
            ids[i] = this.mask(last--);
        }
        return ids;
    }

    @Override
    public int currentFlowId() {
        return this.mask(this.currentValue.get());
    }

    private int mask(int input) {
        return input & MAX_FLOW_ID;
    }
}
