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

package io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.annotation.XtreamRequestBody;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.codec.XtreamMessageCodec;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodArgumentResolver;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 */
public class XtreamRequestBodyArgumentResolver implements XtreamHandlerMethodArgumentResolver {
    private final XtreamMessageCodec messageCodec;

    public XtreamRequestBodyArgumentResolver(EntityCodec entityCodec) {
        this.messageCodec = new XtreamMessageCodec(entityCodec);
    }

    @Override
    public boolean supportsParameter(XtreamMethodParameter parameter) {
        return parameter.getParameterAnnotation(XtreamRequestBody.class).isPresent();
    }

    @Override
    public Mono<Object> resolveArgument(XtreamMethodParameter parameter, XtreamExchange exchange) {
        if (ByteBuf.class.isAssignableFrom(parameter.getParameterType())) {
            return Mono.justOrEmpty(exchange.request().payload());
        }
        final Object instance = this.messageCodec.decode(parameter.getParameterType(), exchange.request().payload().slice());
        return Mono.justOrEmpty(instance);
    }
}
