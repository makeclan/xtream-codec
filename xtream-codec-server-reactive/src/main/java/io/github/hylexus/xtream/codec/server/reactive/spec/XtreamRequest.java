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
import io.netty.buffer.ByteBufAllocator;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author hylexus
 */
public interface XtreamRequest {

    enum Type {
        TCP,
        UDP
    }

    Type type();

    ByteBufAllocator bufferFactory();

    NettyInbound underlyingInbound();

    String getId();

    ByteBuf payload();

    InetSocketAddress remoteAddress();

    Map<String, Object> attributes();

    Mono<XtreamSession> session();

    XtreamRequestBuilder mutate();

    interface XtreamRequestBuilder {

        XtreamRequestBuilder payload(ByteBuf payload);

        XtreamRequestBuilder remoteAddress(InetSocketAddress remoteAddress);

        XtreamRequestBuilder session(Mono<XtreamSession> session);

        XtreamRequest build();
    }
}
