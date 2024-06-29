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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 当前类是从 `org.springframework.web.server.handler.FilteringWebHandler` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.server.handler.FilteringWebHandler`.
 *
 * @author hylexus
 */
public class FilteringXtreamHandler implements XtreamHandler {
    private final XtreamFilterChain chain;

    public FilteringXtreamHandler(XtreamHandler handler) {
        this(handler, Collections.emptyList());
    }

    public FilteringXtreamHandler(XtreamHandler handler, List<XtreamFilter> filters) {
        this.chain = new DefaultXtreamFilterChain(handler, filters);
    }

    @Override
    public Mono<Void> handle(XtreamExchange exchange) {
        return this.chain.filter(exchange);
    }
}
