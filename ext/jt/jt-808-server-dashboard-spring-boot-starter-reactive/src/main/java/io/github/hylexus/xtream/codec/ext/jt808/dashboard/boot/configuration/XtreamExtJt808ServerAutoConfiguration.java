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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.boot.configuration;

import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.actuate.request.DefaultJt808DashboardErrorReporter;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.actuate.request.DefaultJt808DashboardErrorStore;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.actuate.request.Jt808DashboardErrorReporter;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.actuate.request.Jt808DashboardErrorStore;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.boot.properties.XtreamJt808ServerDashboardProperties;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.handler.Jt808ServerDashboardWebFluxConfigurer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@AutoConfiguration
@Import({
        BuiltinJt808DashboardConfiguration.class,
})
@EnableConfigurationProperties({
        XtreamJt808ServerProperties.class,
        XtreamJt808ServerDashboardProperties.class,
})
@ConditionalOnProperty(prefix = "jt808-server.dashboard", name = "enabled", havingValue = "true", matchIfMissing = true)
public class XtreamExtJt808ServerAutoConfiguration {

    private final XtreamJt808ServerDashboardProperties dashboardProperties;

    public XtreamExtJt808ServerAutoConfiguration(XtreamJt808ServerDashboardProperties dashboardProperties) {
        this.dashboardProperties = dashboardProperties;
    }

    @Bean
    Jt808ServerDashboardWebFluxConfigurer jt808ServerDashboardWebFluxConfigurer() {
        return new Jt808ServerDashboardWebFluxConfigurer(this.dashboardProperties);
    }

    @Bean
    public RouterFunction<ServerResponse> dashboardRoutes() {
        final String basePath = this.dashboardProperties.getFormatedBasePath();
        final Mono<ServerResponse> dashboardIndex = ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .bodyValue(new ClassPathResource(Jt808ServerDashboardWebFluxConfigurer.DASHBOARD_STATIC_RESOURCE_DIR + "index.html"));
        // `/dashboard-ui/` ==> index.html
        RouterFunction<ServerResponse> routerFunction = route(GET(basePath), request -> dashboardIndex);
        if ("/".equals(basePath)) {
            return routerFunction;
        }
        // `/dashboard-ui` ==> index.html
        routerFunction = routerFunction.and(route(GET(basePath.substring(0, basePath.length() - 1)), request -> dashboardIndex));
        if (this.dashboardProperties.isRedirectRootToBasePath()) {
            // `/` ==> redirect to `/dashboard-ui/`
            routerFunction = routerFunction.and(route(GET("/"), request ->
                    ServerResponse.temporaryRedirect(URI.create(basePath)).build()
            ));
        }

        return routerFunction;
    }

    // region error-reporter
    @Bean
    @ConditionalOnMissingBean
    Jt808DashboardErrorStore jt808DashboardErrorStore() {
        return new DefaultJt808DashboardErrorStore();
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808DashboardErrorReporter jt808DashboardErrorReporter(Jt808DashboardErrorStore errorStore) {
        return new DefaultJt808DashboardErrorReporter(errorStore);
    }
    // endregion error-reporter
}
