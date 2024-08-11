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

import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.core.annotation.XtreamResponseBody;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResultHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 * @see EntityCodec
 */
public class XtreamResponseBodyHandlerResultHandler implements XtreamHandlerResultHandler {

    private final EntityCodec entityCodec;

    public XtreamResponseBodyHandlerResultHandler(EntityCodec entityCodec) {
        this.entityCodec = entityCodec;
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

        final Flux<ByteBuf> byteBufFlux = this.encode(exchange.bufferFactory(), result);
        return exchange.response().writeWith(byteBufFlux);
    }

    public Flux<ByteBuf> encode(ByteBufAllocator allocator, XtreamHandlerResult handlerResult) {
        final Object returnValue = handlerResult.getReturnValue();
        return switch (returnValue) {
            case null -> Flux.error(new IllegalArgumentException("message is null"));
            case Mono<?> mono -> mono.map(msg -> this.doEncode(msg, allocator)).flux();
            case Flux<?> flux -> flux.map(msg -> this.doEncode(msg, allocator));
            case Object obj -> Flux.just(this.doEncode(obj, allocator));
        };
    }

    protected ByteBuf doEncode(Object message, ByteBufAllocator allocator) {
        if (message instanceof ByteBuf byteBuf) {
            return byteBuf;
        }
        final ByteBuf buffer = allocator.buffer();
        try {
            this.entityCodec.encode(message, buffer);
        } catch (Throwable e) {
            buffer.release();
            throw new RuntimeException(e);
        }
        return buffer;
    }

    @Override
    public int order() {
        return OrderedComponent.BUILTIN_COMPONENT_PRECEDENCE;
    }
}
