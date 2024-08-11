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

package io.github.hylexus.xtream.debug.codec.server.reactive.udp.filter;

import io.github.hylexus.xtream.codec.server.reactive.spec.UdpXtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;

public class PackageDelimiterRemoverFilter implements UdpXtreamFilter {

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        final ByteBuf originalPayload = exchange.request().payload();

        // 仅仅是个示例(808报文)
        // 以 0x7e 开头 && 以 0x7e 结尾 ==> 去掉开头和结尾的 0x7e(示例项目的解码器不需要分隔符)
        if (originalPayload.readableBytes() > 2
                && originalPayload.getByte(0) == 0x7e
                && originalPayload.getByte(originalPayload.readableBytes() - 1) == 0x7e) {

            final XtreamExchange mutatedExchange = exchange.mutate().request(requestBuilder -> {
                final ByteBuf newPayload = originalPayload.slice(1, originalPayload.readableBytes() - 2);
                requestBuilder.payload(newPayload);
            }).build();

            return chain.filter(mutatedExchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int order() {
        return -1;
    }
}
