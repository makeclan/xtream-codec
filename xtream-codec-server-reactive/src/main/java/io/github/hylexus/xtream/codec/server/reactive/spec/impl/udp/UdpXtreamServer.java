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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp;

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamServer;
import reactor.netty.DisposableChannel;
import reactor.netty.udp.UdpServer;
import reactor.netty.udp.UdpServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 */
public class UdpXtreamServer extends AbstractXtreamServer<UdpServer, UdpServerConfig, UdpNettyServerCustomizer> {
    private final List<UdpNettyServerCustomizer> nettyServerCustomizers;

    public UdpXtreamServer() {
        this(List.of());
    }

    public UdpXtreamServer(List<UdpNettyServerCustomizer> nettyServerCustomizers) {
        final List<UdpNettyServerCustomizer> customizers = new ArrayList<>(nettyServerCustomizers);
        customizers.add(new UdpNettyServerCustomizer.Default());
        this.nettyServerCustomizers = OrderedComponent.sort(customizers);
    }

    @Override
    protected DisposableChannel initServer() {
        UdpServer udpServer = UdpServer.create();
        for (final UdpNettyServerCustomizer customizer : this.nettyServerCustomizers) {
            udpServer = customizer.customize(udpServer);
        }
        return udpServer.bindNow();
    }

    @Override
    protected List<UdpNettyServerCustomizer> getCustomizers() {
        return this.nettyServerCustomizers;
    }

    @Override
    public ServerType getServerType() {
        return ServerType.UDP;
    }
}
