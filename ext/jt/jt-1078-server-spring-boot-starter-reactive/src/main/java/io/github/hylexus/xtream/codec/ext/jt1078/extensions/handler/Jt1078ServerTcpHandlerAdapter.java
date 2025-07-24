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

package io.github.hylexus.xtream.codec.ext.jt1078.extensions.handler;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.DefaultTcpXtreamNettyHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;

public class Jt1078ServerTcpHandlerAdapter extends DefaultTcpXtreamNettyHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Jt1078ServerTcpHandlerAdapter.class);
    protected final Scheduler scheduler;

    public Jt1078ServerTcpHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator xtreamExchangeCreator, XtreamHandler xtreamHandler, Scheduler scheduler) {
        super(allocator, xtreamExchangeCreator, xtreamHandler);
        this.scheduler = scheduler;
    }

    @Override
    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
        return nettyInbound.receive()
                // .onBackpressureBuffer(1024,(buffer)->{
                //     log.error("Buffer Overflow");
                // }, BufferOverflowStrategy.DROP_OLDEST)
                .doOnNext(ByteBuf::retain)
                .publishOn(scheduler)
                .flatMap(byteBuf -> {
                    if (byteBuf.readableBytes() <= 0) {
                        byteBuf.release();
                        return Mono.empty();
                    }
                    final InetSocketAddress remoteAddress = this.initTcpRemoteAddress(nettyInbound);
                    return this.handleSingleRequest(nettyInbound, nettyOutbound, byteBuf, remoteAddress)
                            .doFinally(signalType -> byteBuf.release())
                            .onErrorResume(Throwable.class, throwable -> {
                                log.error("Unexpected Exception", throwable);
                                return Mono.empty();
                            });
                })
                .onErrorResume(throwable -> {
                    log.error("Unexpected Error", throwable);
                    return Mono.empty();
                });
    }

    protected Mono<Void> handleSingleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf payload, InetSocketAddress remoteAddress) {
        final XtreamExchange exchange = this.xtreamExchangeCreator.createTcpExchange(allocator, nettyInbound, nettyOutbound, payload, remoteAddress);
        return this.doTcpExchange(exchange).doFinally(signalType -> {
            // ...
            // exchange.request().release();
        });
    }

}
