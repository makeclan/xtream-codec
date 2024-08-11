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
import io.github.hylexus.xtream.codec.ext.jt808.filter.Jt808PackageDelimiterRemoverFilter;
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
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import reactor.netty.udp.UdpServer;

import java.util.List;

import static io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant.*;

@ConditionalOnProperty(prefix = "jt808-server.udp-server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BuiltinJt808ServerUdpConfiguration {

    @Bean
    Jt808PackageDelimiterRemoverFilter jt808PackageDelimiterRemoverFilter() {
        return new Jt808PackageDelimiterRemoverFilter();
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
    UdpXtreamServer udpXtreamServer(
            UdpXtreamNettyHandlerAdapter udpXtreamNettyHandlerAdapter,
            ObjectProvider<UdpNettyServerCustomizer> customizers) {

        return XtreamServerBuilder.newUdpServerBuilder()
                .addServerCustomizer(server -> server.handle(udpXtreamNettyHandlerAdapter))
                .addServerCustomizer(server -> server.doOnChannelInit((observer, channel, remoteAddress) -> {
                    // stripDelimiter=true
                    final DelimiterBasedFrameDecoder frameDecoder = new DelimiterBasedFrameDecoder(DEFAULT_MAX_PACKAGE_SIZE, true, Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}));
                    channel.pipeline().addFirst(BEAN_NAME_CHANNEL_INBOUND_HANDLER_ADAPTER, frameDecoder);
                }))
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
