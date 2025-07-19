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

package io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.configuration.servlet;

import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.properties.XtreamJt808ServerDashboardProperties;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.controller.servlet.BuiltinJt1078DashboardProxyControllerServlet;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.controller.servlet.BuiltinJt1078SubscriptionHttpHandlerServlet;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.controller.servlet.BuiltinJt1078SubscriptionWebSocketHandlerServlet;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.service.Jt808ProxyServiceServlet;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.service.impl.DefaultJt808ProxyServiceServlet;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078RequestPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

@EnableWebSocket
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class XtreamExtJt1078ServerAutoConfigurationServlet {

    @Bean
    Jt808ProxyServiceServlet jt808ProxyService(XtreamJt808ServerDashboardProperties properties) {
        return new DefaultJt808ProxyServiceServlet(properties);
    }

    @Bean
    BuiltinJt1078DashboardProxyControllerServlet builtinJt1078DashboardProxyControllerServlet(Jt808ProxyServiceServlet proxyServiceServlet) {
        return new BuiltinJt1078DashboardProxyControllerServlet(proxyServiceServlet);
    }

    @Bean
    BuiltinJt1078SubscriptionHttpHandlerServlet builtinJt1078SubscriptionHttpHandlerServlet(Jt1078RequestPublisher publisher) {
        return new BuiltinJt1078SubscriptionHttpHandlerServlet(publisher);
    }

    @Bean
    public BuiltinJt1078SubscriptionWebSocketHandlerServlet builtinJt1078SubscriptionWebSocketHandlerServlet(Jt1078RequestPublisher publisher) {
        return new BuiltinJt1078SubscriptionWebSocketHandlerServlet(publisher);
    }

    @Bean
    WebSocketConfigurer builtinJt1078SubscriptionWebSocketConfigurer(BuiltinJt1078SubscriptionWebSocketHandlerServlet webSocketHandler) {
        return registry -> {
            // ...
            registry.addHandler(webSocketHandler, BuiltinJt1078SubscriptionWebSocketHandlerServlet.PATH_PATTERN)
                    .setAllowedOrigins("*");
        };
    }

}
