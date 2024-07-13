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

package io.github.hylexus.xtream.debug.codec.server.reactive.udp;


import io.github.hylexus.xtream.codec.server.reactive.spec.impl.LoggingXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.XtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpXtreamNettyHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XtreamServerReactiveUdpDebugApp {

    private static final Logger log = LoggerFactory.getLogger(XtreamServerReactiveUdpDebugApp.class);

    public static void main(String[] args) {
        XtreamServerBuilder.newUdpServerBuilder()
                .addCustomizer(server -> server.doOnBind(config -> log.info("doOnBind,{}", config))
                        .doOnBound(conn -> log.info("doOnBound {}", conn))
                        .doOnUnbound(connection -> log.info("doOnUnbound, {}", connection))
                        .wiretap(true)
                        .host("localhost")
                        .port(8989)
                        .doOnChannelInit((observer, channel, remoteAddress) -> log.info("doOnChannelInit {}", channel))
                )
                .addCustomizer(server -> server.handle(
                        UdpXtreamNettyHandlerAdapter.newDefaultBuilder()
                                .addHandlerMapping(new DemoUdpXtreamHandlerMapping())
                                .enableBuiltinHandlerAdapters()
                                .enableBuiltinHandlerResultHandlers()
                                .addFilter(new LoggingXtreamFilter())
                                .build()
                ))
                .build()
                .start();
    }
}
