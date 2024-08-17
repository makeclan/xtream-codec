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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.handler;


import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamRequestHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * @author hylexus
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@XtreamRequestHandler
public @interface Jt808RequestHandler {

    @AliasFor(annotation = XtreamRequestHandler.class, attribute = "nonBlockingScheduler")
    String nonBlockingScheduler() default XtreamSchedulerRegistry.SCHEDULER_NAME_NON_BLOCKING;

    @AliasFor(annotation = XtreamRequestHandler.class, attribute = "blockingScheduler")
    String blockingScheduler() default XtreamSchedulerRegistry.SCHEDULER_NAME_BLOCKING;

}
