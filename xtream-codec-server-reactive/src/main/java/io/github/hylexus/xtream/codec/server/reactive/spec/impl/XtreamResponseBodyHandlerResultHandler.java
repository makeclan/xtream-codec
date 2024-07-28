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

import io.github.hylexus.xtream.codec.common.bean.XtreamTypeDescriptor;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.annotation.XtreamResponseBody;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.codec.XtreamMessageCodec;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 * @see EntityCodec
 */
public class XtreamResponseBodyHandlerResultHandler implements XtreamHandlerResultHandler {

    private final XtreamMessageCodec encoder;

    public XtreamResponseBodyHandlerResultHandler(EntityCodec entityCodec) {
        this.encoder = new XtreamMessageCodec(entityCodec);
    }

    @Override
    public boolean supports(XtreamHandlerResult result) {
        return result.getReturnType().getContainerClass().isAnnotationPresent(XtreamResponseBody.class)
                || result.getReturnType().hasMethodAnnotation(XtreamResponseBody.class);
    }

    @Override
    public Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult result) {
        if (result.getReturnValue() == null) {
            return Mono.empty();
        }

        final Publisher<?> publisher = this.adaptReturnValue(result.getReturnValue());

        final Flux<ByteBuf> byteBufFlux = this.encoder.encode(publisher, exchange.bufferFactory(), XtreamTypeDescriptor.fromMethodReturnType(result.getReturnType()));
        return exchange.response().writeWith(byteBufFlux);
    }

    protected Publisher<?> adaptReturnValue(Object returnValue) {
        if (returnValue instanceof Publisher<?> publisher) {
            return publisher;
        }
        return Mono.just(returnValue);
    }
}
