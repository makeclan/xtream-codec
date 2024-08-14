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

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808UdpDatagramPackageSplitter;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808UdpDatagramPackageSplitter;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.filter.udp.Jt808UpDatagramPackageSplitterFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.TcpXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.UdpXtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpNettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpXtreamServer;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.DefaultUdpXtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.UdpXtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.XtreamNettyResourceFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import reactor.netty.udp.UdpServer;

import java.util.List;

@ConditionalOnProperty(prefix = "jt808-server.udp-server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BuiltinJt808ServerUdpConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "jt808-server.udp-server", name = "enable-builtin-multiple-udp-package-splitter", havingValue = "true", matchIfMissing = true)
    Jt808UdpDatagramPackageSplitter jt808UpDatagramPackageSplitter() {
        return new DefaultJt808UdpDatagramPackageSplitter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808-server.udp-server", name = "enable-builtin-multiple-udp-package-splitter", havingValue = "true", matchIfMissing = true)
    Jt808UpDatagramPackageSplitterFilter jt808UpDatagramPackageSplitterFilter(Jt808UdpDatagramPackageSplitter splitter) {
        return new Jt808UpDatagramPackageSplitterFilter(splitter);
    }

    @Bean
    @ConditionalOnMissingBean
    UdpXtreamNettyHandlerAdapter udpXtreamNettyHandlerAdapter(
            List<XtreamHandlerMapping> handlerMappings,
            List<XtreamHandlerAdapter> handlerAdapters,
            List<XtreamHandlerResultHandler> handlerResultHandlers,
            List<XtreamFilter> xtreamFilters,
            List<XtreamRequestExceptionHandler> exceptionHandlers) {

        return XtreamNettyHandlerAdapter.newUdpBuilder()
                .addHandlerMappings(handlerMappings)
                .addHandlerAdapters(handlerAdapters)
                .addHandlerResultHandlers(handlerResultHandlers)
                .addFilters(xtreamFilters.stream().filter(it -> !(it instanceof TcpXtreamFilter)).toList())
                .addExceptionHandlers(exceptionHandlers)
                .build();
    }

    @Bean
    DefaultUdpServerCustomizer defaultUdpServerCustomizer(XtreamJt808ServerProperties serverProps) {
        return new DefaultUdpServerCustomizer(serverProps);
    }

    @Bean
    @ConditionalOnMissingBean
    UdpXtreamNettyResourceFactory udpXtreamNettyResourceFactory(XtreamJt808ServerProperties serverProperties) {
        final XtreamJt808ServerProperties.UdpLoopResourcesProperty loopResources = serverProperties.getUdpServer().getLoopResources();
        return new DefaultUdpXtreamNettyResourceFactory(new XtreamNettyResourceFactory.LoopResourcesProperty(
                loopResources.getThreadNamePrefix(),
                loopResources.getSelectCount(),
                loopResources.getWorkerCount(),
                loopResources.isDaemon(),
                loopResources.isColocate(),
                loopResources.isPreferNative()
        ));
    }

    @Bean
    @ConditionalOnMissingBean
    UdpXtreamServer udpXtreamServer(
            UdpXtreamNettyHandlerAdapter udpXtreamNettyHandlerAdapter,
            UdpXtreamNettyResourceFactory resourceFactory,
            ObjectProvider<UdpNettyServerCustomizer> customizers) {

        return XtreamServerBuilder.newUdpServerBuilder()
                // handler
                .addServerCustomizer(server -> server.handle(udpXtreamNettyHandlerAdapter))
                // loopResources
                .addServerCustomizer(server -> server.runOn(resourceFactory.loopResources(), resourceFactory.preferNative()))
                // 用户自定义配置
                .addServerCustomizers(customizers.stream().toList())
                .build();
    }

    public static class DefaultUdpServerCustomizer implements UdpNettyServerCustomizer {
        private final XtreamJt808ServerProperties serverProps;

        public DefaultUdpServerCustomizer(XtreamJt808ServerProperties serverProps) {
            this.serverProps = serverProps;
        }

        public int order() {
            return OrderedComponent.HIGHEST_PRECEDENCE + 1;
        }

        @Override
        public UdpServer customize(UdpServer server) {
            final XtreamJt808ServerProperties.UdpServerProps udpServer = serverProps.getUdpServer();
            if (StringUtils.hasText(udpServer.getHost())) {
                server = server.host(udpServer.getHost());
            }
            return server.port(udpServer.getPort());
        }
    }
}
