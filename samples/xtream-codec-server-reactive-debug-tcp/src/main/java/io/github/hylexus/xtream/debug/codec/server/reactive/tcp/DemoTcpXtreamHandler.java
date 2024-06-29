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

package io.github.hylexus.xtream.debug.codec.server.reactive.tcp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class DemoTcpXtreamHandler implements XtreamHandler {

    private static final Logger log = LoggerFactory.getLogger(DemoTcpXtreamHandler.class);

    @Override
    public Mono<Void> handle(XtreamExchange exchange) {
        log.info("{}", exchange.request().remoteAddress());
        final ByteBuf byteBuf = exchange.request().body();
        final CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer(2);
        compositeByteBuf.addComponents(true, byteBuf.copy());
        compositeByteBuf.addComponents(true, Unpooled.wrappedBuffer(new byte[]{1, 1, 1, 1}));
        return exchange.response()
                .writeWith(Mono.just(compositeByteBuf));
    }
}
