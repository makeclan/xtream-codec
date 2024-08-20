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
