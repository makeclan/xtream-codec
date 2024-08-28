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

        return this.doProcessJt808Request(exchange, chain, jt808Request).doFinally(signalType -> {
            // ...
            jt808Request.release();
        });
    }

    protected Mono<Void> doProcessJt808Request(XtreamExchange exchange, XtreamFilterChain chain, Jt808Request jt808Request) {
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

    protected XtreamExchange mutatedExchange(XtreamExchange exchange, Jt808Request request) {
        return exchange.mutate().request(request).build();
    }

    @Override
    public int order() {
        return ORDER;
    }
}
