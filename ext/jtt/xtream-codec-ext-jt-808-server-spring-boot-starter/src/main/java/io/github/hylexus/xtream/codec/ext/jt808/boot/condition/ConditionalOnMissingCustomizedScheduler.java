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

package io.github.hylexus.xtream.codec.ext.jt808.boot.condition;

import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.scheduler.SchedulerType;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.*;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ConditionalOnMissingCustomizedScheduler.ConditionalOnMissingCustomizedSchedulerCondition.class)
public @interface ConditionalOnMissingCustomizedScheduler {

    enum Type {
        BLOCKING,
        NON_BLOCKING,
    }

    Type type();

    class ConditionalOnMissingCustomizedSchedulerCondition implements Condition {

        @Override
        public boolean matches(@Nonnull ConditionContext context, @Nonnull AnnotatedTypeMetadata metadata) {
            final Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnMissingCustomizedScheduler.class.getName());
            if (attributes == null || attributes.isEmpty()) {
                return false;
            }
            final Type type = (Type) attributes.get("type");
            if (type == null) {
                return false;
            }

            return this.doMatch(type, context);
        }

        private boolean doMatch(Type type, ConditionContext context) {
            final String property = type == Type.NON_BLOCKING
                    ? context.getEnvironment().getProperty("jt808-server.handler-schedulers.non-blocking-scheduler.type")
                    : context.getEnvironment().getProperty("jt808-server.handler-schedulers.blocking-scheduler.type");
            return !SchedulerType.CUSTOMIZED.name().equalsIgnoreCase(property);
        }
    }
}
