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

package io.github.hylexus.xtream.codec.ext.jt808.utils;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808AttachmentSessionManager;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.ext.jt808.spec.XtreamTcpHeatBeatHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.SessionIdleStateCheckerProps;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpNettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpNettyServerCustomizer;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.util.StringUtils;
import reactor.netty.Connection;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant.BEAN_NAME_CHANNEL_INBOUND_IDLE_STATE_HANDLER;
import static io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant.BEAN_NAME_CHANNEL_INBOUND_IDLE_STATE_HANDLER_CALLBACK;

public final class BuiltinConfigurationUtils {
    private BuiltinConfigurationUtils() {
    }

    public static TcpNettyServerCustomizer defaultTcpBasicConfigurer(String host, int port) {
        return server -> {
            if (StringUtils.hasText(host)) {
                server = server.host(host);
            }
            return server.port(port);
        };
    }

    public static UdpNettyServerCustomizer defaultUdpBasicConfigurer(String host, int port) {
        return server -> {
            if (StringUtils.hasText(host)) {
                server = server.host(host);
            }
            return server.port(port);
        };
    }

    public static void addIdleStateHandler(SessionIdleStateCheckerProps props, Jt808SessionManager sessionManager, Jt808AttachmentSessionManager attachmentSessionManager, Connection connection) {
        connection.addHandlerLast(
                BEAN_NAME_CHANNEL_INBOUND_IDLE_STATE_HANDLER,
                new IdleStateHandler(0, 0, props.getMaxIdleTime().toMillis(), TimeUnit.MILLISECONDS)
        );
        connection.addHandlerLast(
                BEAN_NAME_CHANNEL_INBOUND_IDLE_STATE_HANDLER_CALLBACK,
                new XtreamTcpHeatBeatHandler(sessionManager, attachmentSessionManager)
        );
    }

}
