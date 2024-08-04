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

package io.github.hylexus.xtream.codec.ext.jt808.filter;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import reactor.core.publisher.Mono;

public class Jt808RequestDecoderFilter implements XtreamFilter {

    protected final Jt808RequestDecoder jt808RequestDecoder;

    public Jt808RequestDecoderFilter(Jt808RequestDecoder jt808RequestDecoder) {
        this.jt808RequestDecoder = jt808RequestDecoder;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        final XtreamRequest originalRequest = exchange.request();
        // 将原始的 XtreamRequest 解析为 JTT/808 格式的请求
        final XtreamRequest jt808Request = this.jt808RequestDecoder.decode(originalRequest.bufferFactory(), originalRequest.underlyingInbound(), originalRequest.payload());
        return chain.filter(exchange.mutate().request(jt808Request).build());
    }
}
