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

import java.util.List;
import java.util.ListIterator;

/**
 * 当前类是从 `org.springframework.web.server.handler.DefaultWebFilterChain` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.server.handler.DefaultWebFilterChain`.
 *
 * @author hylexus
 */
public class DefaultXtreamFilterChain implements XtreamFilterChain {
    private final List<XtreamFilter> allFilters;
    private final XtreamHandler handler;

    private final XtreamFilter currentFilter;

    private final DefaultXtreamFilterChain chain;

    public DefaultXtreamFilterChain(XtreamHandler handler, List<XtreamFilter> filters) {
        this.allFilters = filters;
        this.handler = handler;

        final DefaultXtreamFilterChain chain = this.initChain(filters, handler);
        this.currentFilter = chain.currentFilter;
        this.chain = chain.chain;
    }

    public DefaultXtreamFilterChain(List<XtreamFilter> filters, XtreamHandler handler, XtreamFilter currentFilter, DefaultXtreamFilterChain chain) {
        this.allFilters = filters;
        this.handler = handler;
        this.currentFilter = currentFilter;
        this.chain = chain;
    }

    private DefaultXtreamFilterChain initChain(List<XtreamFilter> filters, XtreamHandler handler) {
        DefaultXtreamFilterChain chain = new DefaultXtreamFilterChain(filters, handler, null, null);
        final ListIterator<? extends XtreamFilter> iterator = filters.listIterator(filters.size());
        while (iterator.hasPrevious()) {
            chain = new DefaultXtreamFilterChain(filters, handler, iterator.previous(), chain);
        }
        return chain;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange) {
        return Mono.defer(() -> {
                    return this.currentFilter != null && this.chain != null
                            ? invokeFilter(this.currentFilter, this.chain, exchange)
                            : this.handler.handle(exchange);
                }
        );
    }

    private Mono<Void> invokeFilter(XtreamFilter current, DefaultXtreamFilterChain chain, XtreamExchange exchange) {
        final String currentName = current.getClass().getName();
        return current.filter(exchange, chain).checkpoint(currentName + " [DefaultXtreamFilterChain]");
    }
}
