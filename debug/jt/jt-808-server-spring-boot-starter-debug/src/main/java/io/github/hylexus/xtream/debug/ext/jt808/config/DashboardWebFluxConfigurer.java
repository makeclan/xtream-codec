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

package io.github.hylexus.xtream.debug.ext.jt808.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.PathResourceResolver;
import org.springframework.web.reactive.resource.ResourceResolver;
import org.springframework.web.reactive.resource.ResourceResolverChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hylexus
 */
@Configuration
public class DashboardWebFluxConfigurer implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new JtDashboardStaticResourceResolver());
    }

    private static class JtDashboardStaticResourceResolver implements ResourceResolver {
        private final PathResourceResolver defaultResolver = new PathResourceResolver();

        @Override
        @Nonnull
        public Mono<Resource> resolveResource(ServerWebExchange exchange, @Nonnull String requestPath, @Nonnull List<? extends Resource> locations, @Nonnull ResourceResolverChain chain) {
            return defaultResolver
                    .resolveResource(exchange, requestPath, locations, chain)
                    .switchIfEmpty(Mono.just(new ClassPathResource("/static/index.html")));
        }

        @Override
        @Nonnull
        public Mono<String> resolveUrlPath(@Nonnull String resourcePath, @Nonnull List<? extends Resource> locations, @Nonnull ResourceResolverChain chain) {
            return defaultResolver.resolveUrlPath(resourcePath, locations, chain);
        }
    }
}
