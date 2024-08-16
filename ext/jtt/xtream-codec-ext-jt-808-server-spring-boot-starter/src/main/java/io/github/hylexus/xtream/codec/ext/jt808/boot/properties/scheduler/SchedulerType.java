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

package io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler;

import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public enum SchedulerType {
    /**
     * @see reactor.core.scheduler.Schedulers#newBoundedElastic(int, int, ThreadFactory, int)
     */
    BOUNDED_ELASTIC,
    /**
     * @see reactor.core.scheduler.Schedulers#newParallel(int, ThreadFactory)
     */
    PARALLEL,
    /**
     * 基于虚拟线程
     *
     * @see Executors#newVirtualThreadPerTaskExecutor()
     */
    VIRTUAL,
    /**
     * 用户自定义
     */
    CUSTOMIZED,
    /**
     * 仅仅用于测试
     *
     * @see reactor.core.scheduler.Schedulers#newSingle(ThreadFactory)
     */
    @Deprecated
    SINGLE,
    /**
     * 仅仅用于测试
     *
     * @see Schedulers#immediate()
     */
    @Deprecated
    IMMEDIATE,
}
