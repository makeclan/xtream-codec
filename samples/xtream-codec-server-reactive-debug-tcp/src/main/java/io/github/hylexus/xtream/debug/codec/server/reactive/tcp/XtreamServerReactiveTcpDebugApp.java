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


import io.github.hylexus.xtream.codec.server.reactive.spec.impl.*;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamHandlerAdapter;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.util.List;

public class XtreamServerReactiveTcpDebugApp {

    private static final Logger log = LoggerFactory.getLogger(XtreamServerReactiveTcpDebugApp.class);

    public static void main(String[] args) {
        DisposableServer server =
                TcpServer.create()
                        .host("localhost")
                        .port(8888)
                        .doOnConnection(conn -> {
                            log.info("doOnConnection {}", conn);
                        })
                        .doOnChannelInit((observer, channel, remoteAddress) -> {
                            log.info("doOnChannelInit {}", channel);
                            channel.pipeline()
                                    .addFirst(new LoggingHandler("io.github.hylexus.xtream.debug.codec.server.reactive.tcp"))
                                    .addFirst(new DelimiterBasedFrameDecoder(1024, true, Unpooled.copiedBuffer(new byte[]{0x7e})))
                            ;

                        })
                        .handle(new TcpXtreamHandlerAdapter(
                                        new FilteringXtreamHandler(
                                                new DispatcherXtreamHandler(
                                                        List.of(new DemoTcpXtreamHandlerMapping()),
                                                        List.of(new SimpleXtreamHandlerAdapter()),
                                                        List.of(new LoggingXtreamHandlerResultHandler())
                                                ),
                                                List.of(
                                                        new LoggingXtreamFilter(),
                                                        new LoggingXtreamFilter()
                                                )
                                        )
                                )
                        )
                        .bindNow();

        server.onDispose()
                .block();
    }

}
