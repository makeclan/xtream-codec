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

package io.github.hylexus.xtream.codec.ext.jt1078.boot.configuration;

import io.github.hylexus.xtream.codec.common.utils.BufferFactoryHolder;
import io.github.hylexus.xtream.codec.ext.jt1078.boot.configuration.utils.Jt1078ConfigurationUtils;
import io.github.hylexus.xtream.codec.ext.jt1078.boot.properties.XtreamJt1078ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt1078.extensions.Jt1078ServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TcpHeatBeatHandler;
import io.github.hylexus.xtream.codec.ext.jt1078.utils.Jt1078ServerTcpHandlerAdapterBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.TcpXtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.TcpSessionIdleStateCheckerProps;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.SimpleXtreamRequestHandlerHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpNettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamServer;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.DefaultTcpXtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.TcpXtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.XtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.utils.BuiltinConfigurationUtils;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import reactor.netty.Connection;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.xtream.codec.ext.jt1078.utils.Jt1078Constants.*;

@ConditionalOnProperty(prefix = "jt1078-server.tcp-server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BuiltinJt1078ServerTcpConfiguration {

    @Bean
    XtreamHandlerAdapter mockAdapter() {
        return new SimpleXtreamRequestHandlerHandlerAdapter();
    }

    /**
     * @see Jt1078ConfigurationUtils#jt1078RequestFilterPredicateTcp(XtreamFilter)
     */
    @Bean(BEAN_NAME_JT1078_TCP_XTREAM_NETTY_HANDLER_ADAPTER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT1078_TCP_XTREAM_NETTY_HANDLER_ADAPTER)
    TcpXtreamNettyHandlerAdapter tcpXtreamNettyHandlerAdapter(
            BufferFactoryHolder bufferFactoryHolder,
            Jt1078ServerExchangeCreator exchangeCreator,
            List<XtreamHandlerMapping> handlerMappings,
            List<XtreamHandlerAdapter> handlerAdapters,
            List<XtreamHandlerResultHandler> handlerResultHandlers,
            List<XtreamFilter> xtreamFilters,
            List<XtreamRequestExceptionHandler> exceptionHandlers) {

        return new Jt1078ServerTcpHandlerAdapterBuilder(bufferFactoryHolder.getAllocator())
                .setXtreamExchangeCreator(exchangeCreator)
                .addHandlerMappings(handlerMappings)
                .addHandlerAdapters(handlerAdapters)
                .addHandlerResultHandlers(handlerResultHandlers)
                .addFilters(xtreamFilters.stream().filter(Jt1078ConfigurationUtils::jt1078RequestFilterPredicateTcp).toList())
                .addExceptionHandlers(exceptionHandlers)
                .build();
    }

    @Bean(BEAN_NAME_JT1078_TCP_XTREAM_NETTY_RESOURCE_FACTORY)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT1078_TCP_XTREAM_NETTY_RESOURCE_FACTORY)
    TcpXtreamNettyResourceFactory tcpXtreamNettyResourceFactory(XtreamJt1078ServerProperties serverProperties) {
        final XtreamJt1078ServerProperties.TcpLoopResourcesProperty loopResources = serverProperties.getTcpServer().getLoopResources();
        return new DefaultTcpXtreamNettyResourceFactory(new XtreamNettyResourceFactory.LoopResourcesProperty(
                loopResources.getThreadNamePrefix(),
                loopResources.getSelectCount(),
                loopResources.getWorkerCount(),
                loopResources.isDaemon(),
                loopResources.isColocate(),
                loopResources.isPreferNative()
        ));
    }

    @Bean(BEAN_NAME_JT1078_TCP_XTREAM_SERVER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT1078_TCP_XTREAM_SERVER)
    TcpXtreamServer tcpXtreamServer(
            @Qualifier(BEAN_NAME_JT1078_TCP_XTREAM_NETTY_HANDLER_ADAPTER) TcpXtreamNettyHandlerAdapter tcpXtreamNettyHandlerAdapter,
            @Qualifier(BEAN_NAME_JT1078_TCP_XTREAM_NETTY_RESOURCE_FACTORY) TcpXtreamNettyResourceFactory resourceFactory,
            ObjectProvider<TcpNettyServerCustomizer> customizers,
            Jt1078SessionManager sessionManager,
            XtreamJt1078ServerProperties serverProperties) {

        final XtreamJt1078ServerProperties.TcpServerProps tcpServer = serverProperties.getTcpServer();
        return XtreamServerBuilder.newTcpServerBuilder()
                // 默认 host和 port(用户自定义配置可以再次覆盖默认配置)
                .addServerCustomizer(BuiltinConfigurationUtils.defaultTcpBasicConfigurer(tcpServer.getHost(), tcpServer.getPort()))
                // handler
                .addServerCustomizer(server -> server.handle(tcpXtreamNettyHandlerAdapter))
                // 分包 + 空闲检测
                .addServerCustomizer(server -> server.doOnConnection(connection -> {
                    // 空闲检测
                    addTcpIdleStateHandler(sessionManager, serverProperties, connection);
                    // 分包
                    // stripDelimiter=true
                    final int frameLength = serverProperties.getTcpServer().getMaxFrameLength();
                    // todo 自定义分包器
                    final DelimiterBasedFrameDecoder frameDecoder = new DelimiterBasedFrameDecoder(frameLength, true, Unpooled.copiedBuffer(new byte[]{0x30, 0x31, 0x63, 0x64}));
                    connection.addHandlerFirst(BEAN_NAME_JT1078_CHANNEL_FRAME_DECODER, frameDecoder);
                }))
                // loopResources
                .addServerCustomizer(server -> server.runOn(resourceFactory.loopResources(), resourceFactory.preferNative()))
                // 用户自定义配置
                .addServerCustomizers(customizers.stream().toList())
                .build("JT/T-1078");
    }

    private static void addTcpIdleStateHandler(Jt1078SessionManager sessionManager, XtreamJt1078ServerProperties serverProperties, Connection connection) {
        final TcpSessionIdleStateCheckerProps props = serverProperties.getTcpServer().getSessionIdleStateChecker();
        connection.addHandlerLast(
                "xtreamTcpIdleStateHandler",
                new IdleStateHandler(
                        props.getReaderIdleTime().toMillis(),
                        props.getWriterIdleTime().toMillis(),
                        props.getAllIdleTime().toMillis(),
                        TimeUnit.MILLISECONDS
                )
        );
        connection.addHandlerLast(
                "xtreamTcpIdleStateHandlerCallback",
                new Jt1078TcpHeatBeatHandler(sessionManager)
        );
    }

}
