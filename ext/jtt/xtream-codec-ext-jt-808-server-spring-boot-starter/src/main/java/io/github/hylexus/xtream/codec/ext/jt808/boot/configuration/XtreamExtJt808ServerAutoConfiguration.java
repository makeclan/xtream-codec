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
import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.ext.jt808.boot.listener.XtreamExtJt808ServerStartupListener;
import io.github.hylexus.xtream.codec.ext.jt808.filter.Jt808RequestDecoderFilter;
import io.github.hylexus.xtream.codec.ext.jt808.handler.XtreamExtJt808RequestMappingHandlerMapping;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808RequestDecoder;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.LoggingXtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpNettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamServer;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class XtreamExtJt808ServerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(XtreamExtJt808ServerAutoConfiguration.class);

    @Bean
    XtreamExtJt808RequestMappingHandlerMapping xtreamExtJt808RequestMappingHandlerMapping() {
        return new XtreamExtJt808RequestMappingHandlerMapping();
    }

    @Bean
    Jt808MessageProcessor jt808MessageProcessor() {
        // todo bufferFactory
        return new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    }

    @Bean
    Jt808RequestDecoder jt808RequestDecoder(Jt808MessageProcessor jt808MessageProcessor) {
        return new DefaultJt808RequestDecoder(jt808MessageProcessor);
    }

    @Bean
    Jt808RequestDecoderFilter jt808RequestDecoderFilter(Jt808RequestDecoder jt808RequestDecoder) {
        return new Jt808RequestDecoderFilter(jt808RequestDecoder);
    }

    @Bean
    @Order(OrderedComponent.HIGHEST_PRECEDENCE + 1000)
    LoggingXtreamFilter loggingXtreamFilter() {
        return new LoggingXtreamFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    TcpXtreamServer tcpXtreamServer(
            ObjectProvider<TcpNettyServerCustomizer> customizers,
            ObjectProvider<XtreamHandlerMapping> handlerMappings,
            ObjectProvider<XtreamFilter> xtreamFilters
    ) {

        return XtreamServerBuilder.newTcpServerBuilder()
                .addServerCustomizer(server -> server.host("localhost")
                        .port(8888)
                        .doOnConnection(conn -> log.info("doOnConnection {}", conn))
                        .doOnChannelInit((observer, channel, remoteAddress) -> {
                            log.info("doOnChannelInit {}", channel);
                            channel.pipeline().addFirst(
                                    // stripDelimiter=true
                                    new DelimiterBasedFrameDecoder(1024, true, Unpooled.copiedBuffer(new byte[]{0x7e}))
                            );
                        })
                )
                .addServerCustomizer(server -> server.handle(
                        XtreamNettyHandlerAdapter.newTcpBuilder()
                                .addHandlerMappings(handlerMappings.orderedStream().toList())
                                .enableBuiltinHandlerAdapters(EntityCodec.DEFAULT)
                                .enableBuiltinHandlerResultHandlers(EntityCodec.DEFAULT)
                                .addFilters(xtreamFilters.orderedStream().toList())
                                .addExceptionHandler(new LoggingXtreamRequestExceptionHandler())
                                .build()
                ))
                .build();
    }

    @Bean
    XtreamExtJt808ServerStartupListener xtreamExtJt808ServerStartupListener() {
        return new XtreamExtJt808ServerStartupListener();
    }
}
