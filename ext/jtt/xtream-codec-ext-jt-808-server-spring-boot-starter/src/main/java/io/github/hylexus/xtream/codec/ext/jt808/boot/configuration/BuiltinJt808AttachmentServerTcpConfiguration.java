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

import io.github.hylexus.xtream.codec.common.utils.BufferFactoryHolder;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.utils.BuiltinConfigurationUtils;
import io.github.hylexus.xtream.codec.ext.jt808.utils.Jt808AttachmentServerTcpHandlerAdapterBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.TcpXtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.UdpXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpNettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamServer;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.DefaultTcpXtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.TcpXtreamNettyResourceFactory;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.XtreamNettyResourceFactory;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant.*;

/**
 * 附件服务器配置(TCP)
 */
@ConditionalOnProperty(prefix = "jt808-server.tcp-attachment-server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BuiltinJt808AttachmentServerTcpConfiguration {

    @Bean(BEAN_NAME_JT_808_TCP_XTREAM_NETTY_HANDLER_ADAPTER_ATTACHMENT_SERVER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT_808_TCP_XTREAM_NETTY_HANDLER_ADAPTER_ATTACHMENT_SERVER)
    TcpXtreamNettyHandlerAdapter tcpXtreamNettyHandlerAdapter(
            BufferFactoryHolder bufferFactoryHolder,
            List<XtreamHandlerMapping> handlerMappings,
            List<XtreamHandlerAdapter> handlerAdapters,
            List<XtreamHandlerResultHandler> handlerResultHandlers,
            List<XtreamFilter> xtreamFilters,
            List<XtreamRequestExceptionHandler> exceptionHandlers) {

        return new Jt808AttachmentServerTcpHandlerAdapterBuilder(bufferFactoryHolder.getAllocator())
                .addHandlerMappings(handlerMappings)
                .addHandlerAdapters(handlerAdapters)
                .addHandlerResultHandlers(handlerResultHandlers)
                .addFilters(xtreamFilters.stream().filter(it -> !(it instanceof UdpXtreamFilter)).toList())
                .addExceptionHandlers(exceptionHandlers)
                .build();
    }

    @Bean(BEAN_NAME_JT_808_TCP_XTREAM_NETTY_RESOURCE_FACTORY_ATTACHMENT_SERVER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT_808_TCP_XTREAM_NETTY_RESOURCE_FACTORY_ATTACHMENT_SERVER)
    TcpXtreamNettyResourceFactory tcpXtreamNettyResourceFactory(XtreamJt808ServerProperties serverProperties) {
        final XtreamJt808ServerProperties.TcpLoopResourcesProperty loopResources = serverProperties.getTcpInstructionServer().getLoopResources();
        return new DefaultTcpXtreamNettyResourceFactory(new XtreamNettyResourceFactory.LoopResourcesProperty(
                loopResources.getThreadNamePrefix(),
                loopResources.getSelectCount(),
                loopResources.getWorkerCount(),
                loopResources.isDaemon(),
                loopResources.isColocate(),
                loopResources.isPreferNative()
        ));
    }

    @Bean(BEAN_NAME_JT_808_TCP_XTREAM_SERVER_ATTACHMENT_SERVER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT_808_TCP_XTREAM_SERVER_ATTACHMENT_SERVER)
    TcpXtreamServer tcpXtreamServer(
            @Qualifier(BEAN_NAME_JT_808_TCP_XTREAM_NETTY_HANDLER_ADAPTER_INSTRUCTION_SERVER) TcpXtreamNettyHandlerAdapter tcpXtreamNettyHandlerAdapter,
            @Qualifier(BEAN_NAME_JT_808_TCP_XTREAM_NETTY_RESOURCE_FACTORY_INSTRUCTION_SERVER) TcpXtreamNettyResourceFactory resourceFactory,
            ObjectProvider<TcpNettyServerCustomizer> customizers,
            XtreamJt808ServerProperties serverProperties) {

        final XtreamJt808ServerProperties.TcpAttachmentServerProps tcpServer = serverProperties.getTcpAttachmentServer();
        return XtreamServerBuilder.newTcpServerBuilder()
                // 默认 host和 port(用户自定义配置可以再次覆盖默认配置)
                .addServerCustomizer(BuiltinConfigurationUtils.defaultTcpBasicConfigurer(tcpServer.getHost(), tcpServer.getPort()))
                // handler
                .addServerCustomizer(server -> server.handle(tcpXtreamNettyHandlerAdapter))
                // 分包
                .addServerCustomizer(server -> server.doOnChannelInit((observer, channel, remoteAddress) -> {
                    // stripDelimiter=true
                    final DelimiterBasedFrameDecoder frameDecoder = new DelimiterBasedFrameDecoder(DEFAULT_MAX_PACKAGE_SIZE, true, Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}));
                    channel.pipeline().addFirst(BEAN_NAME_CHANNEL_INBOUND_HANDLER_ADAPTER, frameDecoder);
                }))
                // loopResources
                .addServerCustomizer(server -> server.runOn(resourceFactory.loopResources(), resourceFactory.preferNative()))
                // 用户自定义配置
                .addServerCustomizers(customizers.stream().toList())
                .build("ATTACHMENT");
    }
}
