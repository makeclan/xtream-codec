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

import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.NettyOutbound;

/**
 * @author hylexus
 */
public interface XtreamResponse {

    NettyOutbound underlyingOutbound();

    Mono<Void> writeWith(Publisher<? extends ByteBuf> body);

}
