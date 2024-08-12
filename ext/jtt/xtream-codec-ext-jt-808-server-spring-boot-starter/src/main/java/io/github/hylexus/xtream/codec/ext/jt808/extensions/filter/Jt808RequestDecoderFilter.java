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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.filter;

import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import reactor.core.publisher.Mono;

public class Jt808RequestDecoderFilter implements XtreamFilter {
    public static final int ORDER = -100;
    protected final Jt808RequestDecoder jt808RequestDecoder;
    protected final Jt808RequestCombiner requestCombiner;

    public Jt808RequestDecoderFilter(Jt808RequestDecoder jt808RequestDecoder, Jt808RequestCombiner requestCombiner) {
        this.jt808RequestDecoder = jt808RequestDecoder;
        this.requestCombiner = requestCombiner;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        final XtreamRequest originalRequest = exchange.request();

        // 将原始的 XtreamRequest 解析为 JTT/808 格式的请求
        final Jt808Request jt808Request = this.jt808RequestDecoder.decode(originalRequest.bufferFactory(), originalRequest.underlyingInbound(), originalRequest.payload());

        // 不是子包
        if (!jt808Request.header().messageBodyProps().hasSubPackage()) {
            return chain.filter(this.mutatedExchange(exchange, jt808Request));
        }

        // 合并子包
        final Jt808Request mergedRequest = this.requestCombiner.tryMergeSubPackage(jt808Request);
        // 如果还有子包没到达，终止后续流程，直至所有子包都到达
        if (mergedRequest == null) {
            return Mono.empty();
        }

        // 所有子包都已经到达，继续执行后续流程
        final XtreamExchange mutatedExchange = this.mutatedExchange(exchange, mergedRequest);
        return chain.filter(mutatedExchange).doFinally(signalType -> {
            // ...
            mergedRequest.release();
        });
    }

    private XtreamExchange mutatedExchange(XtreamExchange exchange, Jt808Request request) {
        return exchange.mutate().request(request).build();
    }

    @Override
    public int order() {
        return ORDER;
    }
}
