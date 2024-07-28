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

package io.github.hylexus.xtream.codec.server.reactive.spec;

import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamHandlerAdapterBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpXtreamHandlerAdapterBuilder;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.util.function.BiFunction;

/**
 * @author hylexus
 */
public interface XtreamNettyHandlerAdapter extends BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> {
    static TcpXtreamHandlerAdapterBuilder newTcpBuilder() {
        return newTcpBuilder(ByteBufAllocator.DEFAULT);
    }

    static TcpXtreamHandlerAdapterBuilder newTcpBuilder(ByteBufAllocator allocator) {
        return new TcpXtreamHandlerAdapterBuilder(allocator);
    }

    static UdpXtreamHandlerAdapterBuilder newUdpBuilder() {
        return newUdpBuilder(ByteBufAllocator.DEFAULT);
    }

    static UdpXtreamHandlerAdapterBuilder newUdpBuilder(ByteBufAllocator allocator) {
        return new UdpXtreamHandlerAdapterBuilder(allocator);
    }

    @Override
    Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound);

}
