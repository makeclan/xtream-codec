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

package io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamHandlerMethod;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import org.springframework.util.StringUtils;
import reactor.core.scheduler.Scheduler;

public abstract class AbstractXtreamRequestMappingHandlerMapping implements XtreamHandlerMapping {
    protected final XtreamSchedulerRegistry schedulerRegistry;
    protected final XtreamBlockingHandlerMethodPredicate blockingHandlerMethodPredicate;

    protected AbstractXtreamRequestMappingHandlerMapping(XtreamSchedulerRegistry schedulerRegistry, XtreamBlockingHandlerMethodPredicate blockingHandlerMethodPredicate) {
        this.schedulerRegistry = schedulerRegistry;
        this.blockingHandlerMethodPredicate = blockingHandlerMethodPredicate;
    }

    protected Scheduler determineScheduler(XtreamHandlerMethod handlerMethod, String methodLevelScheduler, String blockingScheduler, String nonBlockingScheduler) {
        if (StringUtils.hasText(methodLevelScheduler)) {
            return this.getSchedulerOrThrow(methodLevelScheduler);
        }
        if (this.blockingHandlerMethodPredicate.isBlockingHandlerMethod(handlerMethod)) {
            if (StringUtils.hasText(blockingScheduler)) {
                return this.getSchedulerOrThrow(blockingScheduler);
            }
            throw new IllegalArgumentException("Cannot determine `blockingScheduler`. Because `schedulerName` is EMPTY");
        }
        if (StringUtils.hasText(nonBlockingScheduler)) {
            return this.getSchedulerOrThrow(nonBlockingScheduler);
        }

        throw new IllegalArgumentException("Cannot determine `nonBlockingScheduler`. Because `schedulerName` is EMPTY");
    }

    protected Scheduler getSchedulerOrThrow(String schedulerName) {
        return this.schedulerRegistry.getScheduler(schedulerName)
                .orElseThrow(() -> new IllegalArgumentException("Cannot determine `Scheduler` with name `" + schedulerName + "`"));
    }

}
