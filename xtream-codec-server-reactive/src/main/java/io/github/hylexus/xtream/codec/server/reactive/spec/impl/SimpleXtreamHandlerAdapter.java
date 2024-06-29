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


import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 */
public class SimpleXtreamHandlerAdapter implements XtreamHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return XtreamHandler.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public Mono<XtreamHandlerResult> handle(XtreamExchange exchange, Object handler) {
        final XtreamHandler xtreamHandler = (XtreamHandler) handler;
        return xtreamHandler.handle(exchange).then(Mono.empty());
    }

    @Override
    public int order() {
        return OrderedComponent.BUILTIN_COMPONENT_PRECEDENCE;
    }
}
