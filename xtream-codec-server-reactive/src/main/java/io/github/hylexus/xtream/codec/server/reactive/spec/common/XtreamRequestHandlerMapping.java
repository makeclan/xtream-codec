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
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XtreamRequestHandlerMapping {

    /**
     * 当前处理器方法使用的 {@link reactor.core.scheduler.Scheduler Scheduler} 。
     * <p>
     * 默认逻辑:
     * <ol>
     *     <li>如果用户指定了自定义的调度器名称 {@code scheduler}，就使用 {@link XtreamSchedulerRegistry#getScheduler(String scheduler)} 返回的调度器；
     *     用户未指定自定义调度器时通过后面步骤确定当前处理器方法使用的调度器:</li>
     *     <li>如果当前处理器方法是 <strong style="color:red;">阻塞的</strong>，就使用 {@link XtreamRequestHandler#blockingScheduler()} 或 {@link XtreamSchedulerRegistry#defaultBlockingScheduler()}</li>
     *     <li>如果当前处理器方法是 <strong style="color:red;">非阻塞的</strong>，就使用 {@link XtreamRequestHandler#nonBlockingScheduler()}  或  {@link XtreamSchedulerRegistry#defaultNonBlockingScheduler()} </li>
     * </ol>
     *
     * @apiNote 通过 {@link io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate} 判断处理器方法是否阻塞
     * @see reactor.core.scheduler.Scheduler
     * @see XtreamRequestHandler#nonBlockingScheduler()
     * @see XtreamRequestHandler#blockingScheduler()
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry#getScheduler(String)
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate
     */
    String scheduler() default "";

}
