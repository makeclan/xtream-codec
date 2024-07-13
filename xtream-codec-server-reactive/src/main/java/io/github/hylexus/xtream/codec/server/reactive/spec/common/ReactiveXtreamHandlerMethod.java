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

package io.github.hylexus.xtream.codec.server.reactive.spec.common;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * @author hylexus
 */
public class ReactiveXtreamHandlerMethod extends XtreamHandlerMethod {

    public ReactiveXtreamHandlerMethod(Class<?> containerClass, Method handler) {
        super(containerClass, handler);
    }

    public Mono<XtreamHandlerResult> invoke(Object containerInstance, Object[] args) {
        try {
            final Object result = this.method.invoke(containerInstance, args);
            final XtreamMethodParameter returnType = new XtreamMethodParameter(-1, this.method);
            return Mono.just(new XtreamHandlerResult(this, result, returnType));
        } catch (Throwable e) {
            return Mono.error(e);
        }
    }
}
