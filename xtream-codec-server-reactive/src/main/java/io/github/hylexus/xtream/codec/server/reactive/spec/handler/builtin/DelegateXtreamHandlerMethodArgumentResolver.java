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

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodArgumentResolver;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hylexus
 */
public class DelegateXtreamHandlerMethodArgumentResolver implements XtreamHandlerMethodArgumentResolver {

    public static final DelegateXtreamHandlerMethodArgumentResolver INSTANCE = new DelegateXtreamHandlerMethodArgumentResolver(
            List.of(
                    new XtreamExchangeArgumentResolver(),
                    new XtreamRequestBodyArgumentResolver(),
                    new XtreamRequestArgumentResolver(),
                    new XtreamResponseArgumentResolver(),
                    new XtreamSessionArgumentResolver()
            )
    );

    private final List<XtreamHandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    public DelegateXtreamHandlerMethodArgumentResolver() {
    }

    public DelegateXtreamHandlerMethodArgumentResolver(List<XtreamHandlerMethodArgumentResolver> resolvers) {
        resolvers.stream().filter(Objects::nonNull).forEach(this::addArgumentResolver);
    }


    public DelegateXtreamHandlerMethodArgumentResolver addArgumentResolver(XtreamHandlerMethodArgumentResolver argumentResolver) {
        this.argumentResolvers.add(argumentResolver);
        return this;
    }

    @Override
    public boolean supportsParameter(XtreamMethodParameter parameter) {
        return this.getArgumentResolver(parameter) != null;
    }

    @Override
    public Mono<Object> resolveArgument(XtreamMethodParameter parameter, XtreamExchange exchange) {
        final XtreamHandlerMethodArgumentResolver resolver = this.getArgumentResolver(parameter);
        if (resolver == null) {
            return Mono.error(new IllegalArgumentException("Unsupported argument type: " + parameter));
        }
        return resolver.resolveArgument(parameter, exchange);
    }

    XtreamHandlerMethodArgumentResolver getArgumentResolver(XtreamMethodParameter parameter) {
        // todo codec-server cache
        for (final XtreamHandlerMethodArgumentResolver resolver : this.argumentResolvers) {
            if (resolver.supportsParameter(parameter)) {
                return resolver;
            }
        }
        return null;
    }
}
