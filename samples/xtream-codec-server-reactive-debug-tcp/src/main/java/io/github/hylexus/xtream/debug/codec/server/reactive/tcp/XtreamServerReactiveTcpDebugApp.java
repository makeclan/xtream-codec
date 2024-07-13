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

package io.github.hylexus.xtream.debug.codec.server.reactive.tcp;


import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamRequestExceptionHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamNettyHandlerAdapter;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XtreamServerReactiveTcpDebugApp {

    private static final Logger log = LoggerFactory.getLogger(XtreamServerReactiveTcpDebugApp.class);

    public static void main(String[] args) {
        XtreamServerBuilder.newTcpServerBuilder()
                .addCustomizer(server -> server.host("localhost")
                        .port(8888)
                        .doOnConnection(conn -> log.info("doOnConnection {}", conn))
                        .doOnChannelInit((observer, channel, remoteAddress) -> {
                            log.info("doOnChannelInit {}", channel);
                            channel.pipeline()
                                    .addFirst(new DelimiterBasedFrameDecoder(1024, true, Unpooled.copiedBuffer(new byte[]{0x7e})));
                        })
                )
                .addCustomizer(server -> server.handle(
                        TcpXtreamNettyHandlerAdapter.newDefaultBuilder()
                                .addHandlerMapping(new DemoTcpXtreamHandlerMapping())
                                .addHandlerMapping(new DemoTcpXtreamHandlerMapping2())
                                .enableBuiltinHandlerAdapters()
                                .enableBuiltinHandlerResultHandlers()
                                .addFilter(new LoggingXtreamFilter())
                                .addExceptionHandler(new XtreamRequestExceptionHandler.LoggingXtreamRequestExceptionHandler())
                                .build()
                ))
                .build()
                .start();
    }
}
