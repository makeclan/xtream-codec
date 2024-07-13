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

package io.github.hylexus.xtream.codec.server.reactive.spec.codec;

import io.github.hylexus.xtream.codec.common.bean.XtreamTypeDescriptor;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 */
public class XtreamMessageCodec {
    protected final EntityCodec entityCodec;

    public XtreamMessageCodec(EntityCodec entityCodec) {
        this.entityCodec = entityCodec;
    }

    public boolean canEncode(XtreamTypeDescriptor typeDescriptor) {
        return true;
    }

    public Flux<ByteBuf> encode(Publisher<?> message, ByteBufAllocator allocator, XtreamTypeDescriptor typeDescriptor) {
        return switch (message) {
            case Mono<?> mono -> mono.map(msg -> this.doEncode(msg, allocator)).flux();
            case Flux<?> flux -> flux.map(msg -> this.doEncode(msg, allocator));
            case null -> Flux.error(new IllegalArgumentException("message is null"));
            default -> Flux.error(new IllegalStateException("Unexpected value: " + message));
        };
    }

    protected ByteBuf doEncode(Object message, ByteBufAllocator allocator) {
        final ByteBuf buffer = allocator.buffer();
        try {
            if (message instanceof ByteBuf byteBuf) {
                buffer.writeBytes(byteBuf);
            } else {
                this.entityCodec.encode(message, buffer);
            }
        } catch (Throwable e) {
            buffer.release();
            throw new RuntimeException(e);
        }
        return buffer;
    }

    public Object decode(Class<?> parameterType, ByteBuf buffer) {
        return this.entityCodec.decode(parameterType, buffer);
    }
}
