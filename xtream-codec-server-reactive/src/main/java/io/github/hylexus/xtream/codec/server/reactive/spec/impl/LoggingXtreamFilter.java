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

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 */
public class LoggingXtreamFilter implements XtreamFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingXtreamFilter.class);

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        log.info("==> Receive [{}] message: remoteAddr = {}, payload = {}",
                exchange instanceof TcpXtreamExchange ? "TCP" : "UDP",
                exchange.request().remoteAddress(),
                FormatUtils.toHexString(exchange.request().body())
        );
        return chain.filter(exchange);
    }

    @Override
    public int order() {
        return OrderedComponent.HIGHEST_PRECEDENCE + 1000;
    }
}
