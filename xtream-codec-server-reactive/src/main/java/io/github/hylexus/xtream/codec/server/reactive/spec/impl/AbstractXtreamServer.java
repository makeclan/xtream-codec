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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.server.reactive.spec.NettyServerCustomizer;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.DisposableChannel;
import reactor.netty.transport.Transport;
import reactor.netty.transport.TransportConfig;

import java.util.List;

/**
 * @author hylexus
 */
@SuppressWarnings("checkstyle:ClassTypeParameterName")
public abstract class AbstractXtreamServer<
        T extends Transport<T, TC>,
        TC extends TransportConfig,
        CUS extends NettyServerCustomizer<T, TC>
        >
        implements XtreamServer {

    private static final Logger log = LoggerFactory.getLogger(AbstractXtreamServer.class);
    protected volatile DisposableChannel disposableServer;

    @Override
    public void start() {
        try {
            this.disposableServer = this.initServer();
        } catch (Throwable e) {
            throw new IllegalStateException("Cannot start netty server", e);
        }

        this.doStart();

        log.info("XtreamServer listening on {}({})", this.disposableServer.address(), this.getServerType());
    }

    private void doStart() {
        final Thread thread = new Thread(() -> {
            // ...
            this.disposableServer.onDispose().block();
        }, "XtreamServer");
        thread.setDaemon(false);
        thread.setContextClassLoader(this.getClass().getClassLoader());
        thread.start();
    }

    @Override
    public void stop() {
        if (this.disposableServer != null) {
            this.disposableServer.disposeNow();
        }
        this.disposableServer = null;
    }

    protected abstract DisposableChannel initServer();

    protected abstract List<CUS> getCustomizers();
}
