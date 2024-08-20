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
