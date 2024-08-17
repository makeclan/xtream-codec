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

package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration;

import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.filter.Jt808RequestDecoderFilter;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808RequestMappingHandlerMapping;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBodyHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodArgumentResolver;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.DelegateXtreamHandlerMethodArgumentResolver;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.LoggingXtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.SimpleXtreamRequestHandlerHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamResponseBodyHandlerResultHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;


public class BuiltinJt808ServerHandlerConfiguration {

    // region exceptionHandlers
    @Bean
    LoggingXtreamRequestExceptionHandler loggingXtreamRequestExceptionHandler() {
        return new LoggingXtreamRequestExceptionHandler();
    }
    // endregion exceptionHandlers

    // region filters
    @Bean
    LoggingXtreamFilter loggingXtreamFilter() {
        return new LoggingXtreamFilter();
    }

    @Bean
    Jt808RequestDecoderFilter jt808RequestDecoderFilter(Jt808RequestDecoder jt808RequestDecoder, Jt808RequestCombiner jt808RequestCombiner) {
        return new Jt808RequestDecoderFilter(jt808RequestDecoder, jt808RequestCombiner);
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
    XtreamResponseBodyHandlerResultHandler xtreamResponseBodyHandlerResultHandler(EntityCodec entityCodec) {
        return new XtreamResponseBodyHandlerResultHandler(entityCodec);
    }

    @Bean
    Jt808ResponseBodyHandlerResultHandler jt808ResponseBodyHandlerResultHandler(DefaultJt808ResponseEncoder jt808ResponseEncoder) {
        return new Jt808ResponseBodyHandlerResultHandler(jt808ResponseEncoder);
    }
    // endregion handlerResultHandlers

}
