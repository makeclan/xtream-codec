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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp;

import io.github.hylexus.xtream.codec.server.reactive.spec.TcpXtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

/**
 * @author hylexus
 */
public class DefaultTcpXtreamNettyHandlerAdapter implements TcpXtreamNettyHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(DefaultTcpXtreamNettyHandlerAdapter.class);
    protected final XtreamHandler xtreamHandler;
    protected final ByteBufAllocator allocator;
    protected final XtreamExchangeCreator xtreamExchangeCreator;

    public DefaultTcpXtreamNettyHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator xtreamExchangeCreator, XtreamHandler xtreamHandler) {
        this.xtreamHandler = xtreamHandler;
        this.allocator = allocator;
        this.xtreamExchangeCreator = xtreamExchangeCreator;
        log.info("DefaultTcpXtreamNettyHandlerAdapter initialized");
    }

    @Override
    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
        return nettyInbound.receive().flatMap(byteBuf -> {
            // ...
            return this.handleSingleRequest(nettyInbound, nettyOutbound, byteBuf);
        }).onErrorResume(throwable -> {
            log.error("Unexpected Error", throwable);
            return Mono.empty();
        });
    }

    protected Mono<Void> handleSingleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf payload) {
        if (payload.readableBytes() <= 0) {
            return Mono.empty();
        }

        final XtreamExchange exchange = this.xtreamExchangeCreator.createTcpExchange(allocator, nettyInbound, nettyOutbound, payload);

        return xtreamHandler
                .handle(exchange)
                .doOnError(Throwable.class, throwable -> {
                    // ...
                    log.error(throwable.getMessage(), throwable);
                });
    }
}
