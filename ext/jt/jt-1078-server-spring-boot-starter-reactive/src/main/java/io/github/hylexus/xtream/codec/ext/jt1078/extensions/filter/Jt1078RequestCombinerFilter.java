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

package io.github.hylexus.xtream.codec.ext.jt1078.extensions.filter;

import io.github.hylexus.xtream.codec.ext.jt1078.codec.Jt1078RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078SubPackageIdentifier;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Jt1078RequestCombinerFilter implements XtreamFilter {
    public static final int ORDER = -100;
    private static final Logger log = LoggerFactory.getLogger(Jt1078RequestCombinerFilter.class);
    private final Jt1078RequestCombiner combiner;

    public Jt1078RequestCombinerFilter(Jt1078RequestCombiner combiner) {
        this.combiner = combiner;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        // 不是 Jt1078Request ?
        if (!(exchange.request() instanceof Jt1078Request request)) {
            return chain.filter(exchange);
        }
        // 不是子包
        if (request.header().subPackageIdentifier() == Jt1078SubPackageIdentifier.ATOMIC) {
            return chain.filter(exchange);
        }

        // 合并子包
        final Jt1078Request mergedRequest = this.combiner.tryMergeSubPackage(request);

        // 如果还有子包没到达，终止后续流程，直至所有子包都到达
        if (mergedRequest == null) {
            return Mono.empty();
        }

        // 使用合并后的请求替换原始请求
        final XtreamExchange mutatedExchange = exchange.mutate().request(mergedRequest).build();

        // 继续下一个 Filter
        return chain.filter(mutatedExchange).doFinally(signalType -> {
            // ...
            mergedRequest.release();
        });
    }

    @Override
    public int order() {
        return ORDER;
    }

}
