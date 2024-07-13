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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp;

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.NettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamServer;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import reactor.netty.tcp.TcpServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 */
public class TcpXtreamServer extends AbstractXtreamServer<TcpServer, TcpServerConfig, TcpNettyServerCustomizer> {

    private final List<TcpNettyServerCustomizer> nettyServerCustomizers;

    public TcpXtreamServer(List<TcpNettyServerCustomizer> nettyServerCustomizers) {
        final List<TcpNettyServerCustomizer> customizers = new ArrayList<>(nettyServerCustomizers);
        customizers.add(new TcpNettyServerCustomizer.Default());
        this.nettyServerCustomizers = OrderedComponent.sort(customizers);
    }

    @Override
    protected DisposableServer initServer() {
        TcpServer tcpServer = TcpServer.create();
        for (final NettyServerCustomizer<TcpServer, TcpServerConfig> customizer : this.nettyServerCustomizers) {
            tcpServer = customizer.customize(tcpServer);
        }
        return tcpServer.bindNow();
    }

    @Override
    protected List<TcpNettyServerCustomizer> getCustomizers() {
        return this.nettyServerCustomizers;
    }

    @Override
    public ServerType getServerType() {
        return ServerType.TCP;
    }
}
