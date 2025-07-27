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

import io.github.hylexus.xtream.codec.ext.jt1078.codec.Jt1078ByteToMessageDecoder;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.*;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamResponse;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.DefaultTcpXtreamNettyHandlerAdapter;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;
import java.util.UUID;

public class Jt1078ServerTcpHandlerAdapter extends DefaultTcpXtreamNettyHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Jt1078ServerTcpHandlerAdapter.class);
    protected final Scheduler scheduler;

    public Jt1078ServerTcpHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator xtreamExchangeCreator, XtreamHandler xtreamHandler, Scheduler scheduler) {
        super(allocator, xtreamExchangeCreator, xtreamHandler);
        this.scheduler = scheduler;
    }

    @Override
    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
        return nettyInbound.receiveObject()
                .map(Jt1078ByteToMessageDecoder.Jt1078PackageInfo.class::cast)
                // .onBackpressureBuffer(1024,(buffer)->{
                //     log.error("Buffer Overflow");
                // }, BufferOverflowStrategy.DROP_OLDEST)
                // .doOnNext(it->it.body().retain())
                .publishOn(scheduler)
                .flatMap((Jt1078ByteToMessageDecoder.Jt1078PackageInfo byteBuf) -> {
                    final InetSocketAddress remoteAddress = this.initTcpRemoteAddress(nettyInbound);
                    final XtreamExchange exchange = this.createTcpExchange(allocator, nettyInbound, nettyOutbound, byteBuf, remoteAddress);
                    return this.doTcpExchange(exchange);
                })
                .onErrorResume(throwable -> {
                    log.error("Unexpected Error", throwable);
                    return Mono.empty();
                });
    }

    protected XtreamExchange createTcpExchange(ByteBufAllocator allocator, NettyInbound nettyInbound, NettyOutbound nettyOutbound, Jt1078ByteToMessageDecoder.Jt1078PackageInfo byteBuf, InetSocketAddress remoteAddress) {
        final XtreamRequest.Type type = XtreamRequest.Type.TCP;
        final String requestId = UUID.randomUUID().toString().replace("-", "");
        final XtreamRequest request = new DefaultJt1078Request(
                requestId,
                requestId, allocator,
                nettyInbound,
                XtreamInbound.Type.TCP,
                remoteAddress,
                byteBuf.header(),
                byteBuf.body()
        );
        final DefaultXtreamResponse response = new DefaultXtreamResponse(allocator, nettyOutbound, type, remoteAddress);

        return new DefaultXtreamExchange(sessionManager, request, response);
    }

}
