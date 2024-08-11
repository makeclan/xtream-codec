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

package io.github.hylexus.xtream.codec.ext.jt808.boot.listener;

import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamServer;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpXtreamServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class XtreamExtJt808ServerStartupListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(XtreamExtJt808ServerStartupListener.class);
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final XtreamJt808ServerProperties serverProps;
    private ApplicationContext applicationContext;

    public XtreamExtJt808ServerStartupListener(XtreamJt808ServerProperties serverProps) {
        this.serverProps = serverProps;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        final boolean tcpServerEnabled = serverProps.getTcpServer().isEnabled();
        final boolean udpServerEnabled = serverProps.getUdpServer().isEnabled();

        if (!tcpServerEnabled && !udpServerEnabled) {
            log.error("Both tcpServer and udpServer are disabled, please enable one of them.");
            return;
        }

        if (initialized.compareAndSet(false, true) && event.getApplicationContext().getParent() == null) {
            if (tcpServerEnabled) {
                final TcpXtreamServer tcpServer = applicationContext.getBean(TcpXtreamServer.class);
                tcpServer.start();
            }

            if (udpServerEnabled) {
                final UdpXtreamServer udpServer = applicationContext.getBean(UdpXtreamServer.class);
                udpServer.start();
            }
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
