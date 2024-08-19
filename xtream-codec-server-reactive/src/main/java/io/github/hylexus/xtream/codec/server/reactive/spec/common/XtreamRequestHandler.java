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

package io.github.hylexus.xtream.codec.server.reactive.spec.common;


import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;

import java.lang.annotation.*;


/**
 * @author hylexus
 * @see XtreamRequestHandlerMapping#scheduler()
 * @see XtreamSchedulerRegistry#defaultNonBlockingScheduler()
 * @see XtreamSchedulerRegistry#defaultBlockingScheduler()
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XtreamRequestHandler {

    /**
     * 默认的 <strong color="red">非阻塞</strong> 处理器的调度器。
     * <p>
     * 可以被 {@link XtreamRequestHandlerMapping#scheduler()} 覆盖
     *
     * @see XtreamRequestHandlerMapping#scheduler()
     */
    String nonBlockingScheduler() default XtreamSchedulerRegistry.SCHEDULER_NAME_NON_BLOCKING;

    /**
     * 默认的 <strong color="red">阻塞</strong> 处理器的调度器。
     * <p>
     * 可以被 {@link XtreamRequestHandlerMapping#scheduler()} 覆盖
     *
     * @see XtreamRequestHandlerMapping#scheduler()
     */
    String blockingScheduler() default XtreamSchedulerRegistry.SCHEDULER_NAME_BLOCKING;

}
