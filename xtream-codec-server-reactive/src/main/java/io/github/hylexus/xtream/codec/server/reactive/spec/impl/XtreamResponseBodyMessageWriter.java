/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.annotation.XtreamResponseBody;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamMessageWriter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamOutbound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author hylexus
 */
public class XtreamResponseBodyMessageWriter implements XtreamMessageWriter {
    private final EntityCodec entityCodec;

    public XtreamResponseBodyMessageWriter(EntityCodec entityCodec) {
        this.entityCodec = entityCodec;
    }

    @Override
    public boolean canWrite(XtreamMethodParameter methodParameter) {
        return methodParameter.getContainerClass().isAnnotationPresent(XtreamResponseBody.class)
                || methodParameter.hasMethodAnnotation(XtreamResponseBody.class);
    }

    @Override
    public Mono<Void> write(XtreamMethodParameter methodParameter, XtreamInbound inbound, XtreamOutbound outbound, Object data, Map<String, Object> context) {
        final Flux<ByteBuf> byteBufFlux = this.encode(outbound.bufferFactory(), data);
        return outbound.writeWith(byteBufFlux);
    }

    public Flux<ByteBuf> encode(ByteBufAllocator allocator, Object data) {
        return switch (data) {
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
}
