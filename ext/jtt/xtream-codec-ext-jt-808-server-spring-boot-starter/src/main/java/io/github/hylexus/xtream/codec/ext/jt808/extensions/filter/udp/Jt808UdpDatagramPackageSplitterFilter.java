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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.filter.udp;

import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808UdpDatagramPackageSplitter;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.filter.Jt808RequestDecoderFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.UdpXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个 UDP 包里可能有多个 JT808 数据包，这里使用 {@link Jt808UdpDatagramPackageSplitter} 拆包后再处理。
 */
public class Jt808UdpDatagramPackageSplitterFilter implements UdpXtreamFilter {

    protected final Jt808UdpDatagramPackageSplitter datagramPackageSplitter;

    public Jt808UdpDatagramPackageSplitterFilter(Jt808UdpDatagramPackageSplitter datagramPackageSplitter) {
        this.datagramPackageSplitter = datagramPackageSplitter;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        final ByteBuf originalPayload = exchange.request().payload();
        final List<ByteBuf> byteBufList = this.datagramPackageSplitter.split(originalPayload);

        final List<XtreamExchange> exchanges = new ArrayList<>();

        for (final ByteBuf payload : byteBufList) {
            final XtreamExchange newExchange = exchange.mutate().request(xtreamRequestBuilder -> xtreamRequestBuilder.payload(payload, false)).build();
            exchanges.add(newExchange);
        }

        if (exchanges.size() == 1) {
            final XtreamExchange first = exchanges.getFirst();
            return chain.filter(first).doFinally(signalType -> first.request().release());
        }

        return Flux.fromIterable(exchanges).flatMap(ex -> {
            // ...
            return chain.filter(ex).doFinally(signalType -> ex.request().release());
        }).then();
    }

    @Override
    public int order() {
        return Jt808RequestDecoderFilter.ORDER - 1;
    }
}
