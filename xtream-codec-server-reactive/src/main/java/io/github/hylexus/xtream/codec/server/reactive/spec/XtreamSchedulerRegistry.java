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

package io.github.hylexus.xtream.codec.server.reactive.spec;

import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamConstants;
import reactor.core.scheduler.Scheduler;

import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 */
public interface XtreamSchedulerRegistry {
    String SCHEDULER_NAME_NON_BLOCKING = XtreamConstants.BEAN_NAME_HANDLER_ADAPTER_NON_BLOCKING_SCHEDULER;
    String SCHEDULER_NAME_BLOCKING = XtreamConstants.BEAN_NAME_HANDLER_ADAPTER_BLOCKING_SCHEDULER;

    default Scheduler defaultNonBlockingScheduler() {
        return this.getScheduler(SCHEDULER_NAME_NON_BLOCKING).orElseThrow();
    }

    default Scheduler defaultBlockingScheduler() {
        return this.getScheduler(SCHEDULER_NAME_BLOCKING).orElseThrow();
    }

    /**
     * @param name 调度器名称
     */
    Optional<Scheduler> getScheduler(String name);

    /**
     * @param name      要注册的调度器的名称
     * @param scheduler 要注册的调度器
     * @return {@code true}: 成功注册; {@code false}: 已经存在同名调度器
     */
    boolean registerScheduler(String name, Scheduler scheduler);

    /**
     * @param name 要移除的调度器名称
     * @return {@code true}: 成功移除; {@code false}: 要移除的调度器不存在
     * @throws UnsupportedOperationException 尝试移除默认调度器
     */
    boolean removeScheduler(String name) throws UnsupportedOperationException;

    /**
     * @return 只读视图
     */
    Map<String, Scheduler> asMapView();

}
