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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.util.function.BiFunction;

/**
 * @author hylexus
 */
@Slf4j
public class TcpXtreamHandlerAdapter implements BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> {

    private final XtreamHandler xtreamHandler;

    public TcpXtreamHandlerAdapter(XtreamHandler xtreamHandler) {
        this.xtreamHandler = xtreamHandler;
        log.info("XtreamTcpHandlerAdapter initialized");
    }

    @Override
    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
        return nettyInbound.receive().flatMap(byteBuf -> {
            if (byteBuf.readableBytes() <= 0) {
                return Mono.empty();
            }

            final TcpXtreamSession session = new TcpXtreamSession();
            final TcpXtreamExchange exchange = new TcpXtreamExchange(
                    new TcpXtreamRequest(nettyInbound, session, byteBuf),
                    new TcpXtreamResponse(nettyOutbound),
                    session
            );
            return xtreamHandler
                    .handle(exchange)
                    .doOnError(Throwable.class, throwable -> {
                        // ...
                        log.error(throwable.getMessage(), throwable);
                    });
        }).then();
    }
}
