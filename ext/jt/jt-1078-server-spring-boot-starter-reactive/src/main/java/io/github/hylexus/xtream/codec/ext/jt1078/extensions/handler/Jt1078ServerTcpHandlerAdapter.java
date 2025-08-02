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
import io.github.hylexus.xtream.codec.ext.jt1078.spec.*;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.TcpXtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.UUID;

public class Jt1078ServerTcpHandlerAdapter implements TcpXtreamNettyHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Jt1078ServerTcpHandlerAdapter.class);
    protected final ByteBufAllocator allocator;
    protected final Scheduler scheduler;
    protected final Jt1078SessionManager sessionManager;
    protected final Jt1078RequestHandler jt1078RequestHandler;

    public Jt1078ServerTcpHandlerAdapter(ByteBufAllocator allocator, Scheduler scheduler, Jt1078SessionManager sessionManager, Jt1078RequestHandler jt1078RequestHandler) {
        this.allocator = allocator;
        this.scheduler = scheduler;
        this.sessionManager = sessionManager;
        this.jt1078RequestHandler = jt1078RequestHandler;
    }

    @Override
    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
        final Channel[] channels = new Channel[1];
        nettyInbound.withConnection(connection -> channels[0] = connection.channel());
        final Channel channel = channels[0];
        final String sessionId = this.sessionManager.sessionIdGenerator().generateTcpSessionId(channel);
        final InetSocketAddress remoteAddress = this.initTcpRemoteAddress(nettyInbound);

        return nettyInbound.receiveObject()
                .map(Jt1078ByteToMessageDecoder.Jt1078PackageInfo.class::cast)
                // .onBackpressureBuffer(1024,(buffer)->{
                //     log.error("Buffer Overflow");
                // }, BufferOverflowStrategy.DROP_OLDEST)
                // .doOnNext(it->it.body().retain())
                .publishOn(scheduler)
                .flatMap((Jt1078ByteToMessageDecoder.Jt1078PackageInfo packageInfo) -> {
                    final Jt1078RequestHeader header = packageInfo.header();
                    return this.sessionManager
                            .getOrCreateSession(sessionId, () -> this.createSession(nettyOutbound, sessionId, header, remoteAddress))
                            .flatMap(session -> {
                                session.lastCommunicateTime(Instant.now());
                                final String requestId = UUID.randomUUID().toString().replace("-", "");
                                final Jt1078Request request = new DefaultJt1078Request(
                                        requestId,
                                        allocator,
                                        nettyInbound,
                                        XtreamInbound.Type.TCP,
                                        remoteAddress,
                                        header,
                                        packageInfo.body()
                                );
                                return this.jt1078RequestHandler.handleRequest(session, request);
                            });
                })
                .onErrorResume(throwable -> {
                    log.error("Unexpected Error", throwable);
                    return Mono.empty();
                });
    }

    protected Jt1078Session createSession(NettyOutbound nettyOutbound, String sessionId, Jt1078RequestHeader header, InetSocketAddress remoteAddress) {
        return new DefaultJt1078Session(
                sessionId,
                this.allocator,
                nettyOutbound,
                XtreamInbound.Type.TCP,
                header.convertedSim(),
                header.channelNumber(),
                remoteAddress, this.sessionManager,
                header.rawSim()
        );
    }

    protected InetSocketAddress initTcpRemoteAddress(NettyInbound nettyInbound) {
        final InetSocketAddress[] remoteAddress = new InetSocketAddress[1];
        nettyInbound.withConnection(connection -> remoteAddress[0] = (InetSocketAddress) connection.channel().remoteAddress());
        return remoteAddress[0];
    }

}
