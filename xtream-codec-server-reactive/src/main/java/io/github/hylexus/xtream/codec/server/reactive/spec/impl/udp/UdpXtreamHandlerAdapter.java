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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;
import java.util.function.BiFunction;

/**
 * @author hylexus
 */
@Slf4j
public class UdpXtreamHandlerAdapter implements BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> {

    private final XtreamHandler xtreamHandler;

    public UdpXtreamHandlerAdapter(XtreamHandler xtreamHandler) {
        this.xtreamHandler = xtreamHandler;
        log.info("UdpXtreamHandlerAdapter initialized");
    }

    @Override
    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
        return nettyInbound.receiveObject().flatMap(object -> {
            if (object instanceof DatagramPacket datagramPacket) {
                return handleRequest(nettyInbound, nettyOutbound, datagramPacket);
            } else {
                return Mono.error(new RuntimeException("xxx"));
            }
        });
    }

    private Mono<Void> handleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, DatagramPacket datagramPacket) {
        final InetSocketAddress remoteAddr = datagramPacket.sender();
        final UdpXtreamSession session = new UdpXtreamSession();
        final UdpXtreamExchange exchange = new UdpXtreamExchange(
                new UdpXtreamRequest(nettyInbound, session, datagramPacket),
                new UdpXtreamResponse(nettyOutbound, remoteAddr),
                session
        );
        return xtreamHandler
                .handle(exchange)
                .doOnError(Throwable.class, throwable -> {
                    // ...
                    log.error(throwable.getMessage(), throwable);
                });
    }
}
