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

package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration;

import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.ext.jt808.boot.configuration.attachment.BuiltinJt808AttachmentServerConfiguration;
import io.github.hylexus.xtream.codec.ext.jt808.boot.configuration.instruction.BuiltinJt808InstructionServerConfiguration;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.filter.Jt808RequestDecoderFilter;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808RequestMappingHandlerMapping;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBodyHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodArgumentResolver;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.DelegateXtreamHandlerMethodArgumentResolver;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.LoggingXtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.SimpleXtreamRequestHandlerHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamResponseBodyHandlerResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Import({
        BuiltinJt808InstructionServerConfiguration.class,
        BuiltinJt808AttachmentServerConfiguration.class,
})
public class BuiltinJt808ServerHandlerConfiguration {

    // region exceptionHandlers
    @Bean
    LoggingXtreamRequestExceptionHandler loggingXtreamRequestExceptionHandler() {
        return new LoggingXtreamRequestExceptionHandler();
    }
    // endregion exceptionHandlers

    // region filters
    @Bean
    @ConditionalOnProperty(prefix = "jt808-server.builtin-filters.request-logger", name = "enabled", havingValue = "true", matchIfMissing = true)
    LoggingXtreamFilter loggingXtreamFilter() {
        return new LoggingXtreamFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "jt808-server.builtin-filters.request-decoder", name = "enabled", havingValue = "true", matchIfMissing = true)
    Jt808RequestDecoderFilter jt808RequestDecoderFilter(
            Jt808RequestDecoder jt808RequestDecoder,
            Jt808RequestCombiner jt808RequestCombiner,
            Jt808RequestLifecycleListener lifecycleListener) {
        return new Jt808RequestDecoderFilter(jt808RequestDecoder, jt808RequestCombiner, lifecycleListener);
    }
    // endregion filters

    // region handlerMappings
    @Bean
    Jt808RequestMappingHandlerMapping jt808RequestMappingHandlerMapping(
            XtreamSchedulerRegistry schedulerRegistry,
            XtreamBlockingHandlerMethodPredicate blockingHandlerMethodPredicate) {

        return new Jt808RequestMappingHandlerMapping(schedulerRegistry, blockingHandlerMethodPredicate);
    }
    // endregion handlerMappings

    // region handlerAdapters
    @Bean
    @Primary
    XtreamHandlerMethodArgumentResolver xtreamHandlerMethodArgumentResolver(
            List<XtreamHandlerMethodArgumentResolver> resolvers,
            EntityCodec entityCodec) {

        final DelegateXtreamHandlerMethodArgumentResolver resolver = new DelegateXtreamHandlerMethodArgumentResolver();
        resolvers.forEach(resolver::addArgumentResolver);
        resolver.addDefault(entityCodec);
        return resolver;
    }

    @Bean
    XtreamHandlerMethodHandlerAdapter xtreamHandlerMethodHandlerAdapter(XtreamHandlerMethodArgumentResolver argumentResolver) {

        return new XtreamHandlerMethodHandlerAdapter(argumentResolver);
    }

    @Bean
    SimpleXtreamRequestHandlerHandlerAdapter simpleXtreamRequestHandlerHandlerAdapter() {
        return new SimpleXtreamRequestHandlerHandlerAdapter();
    }
    // endregion handlerAdapters

    // region handlerResultHandlers
    @Bean
    LoggingXtreamHandlerResultHandler loggingXtreamHandlerResultHandler() {
        return new LoggingXtreamHandlerResultHandler();
    }

    @Bean
    Jt808ResponseBodyHandlerResultHandler jt808ResponseBodyHandlerResultHandler(Jt808ResponseEncoder jt808ResponseEncoder, Jt808RequestLifecycleListener lifecycleListener) {
        return new Jt808ResponseBodyHandlerResultHandler(jt808ResponseEncoder, lifecycleListener);
    }

    @Bean
    XtreamResponseBodyHandlerResultHandler xtreamResponseBodyHandlerResultHandler(EntityCodec entityCodec) {
        return new XtreamResponseBodyHandlerResultHandler(entityCodec);
    }
    // endregion handlerResultHandlers

    // region session
    @Bean
    @ConditionalOnMissingBean
    XtreamSessionIdGenerator xtreamSessionIdGenerator() {
        return new XtreamSessionIdGenerator.DefalutXtreamSessionIdGenerator();
    }

    // endregion session
}
