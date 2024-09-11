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

package io.github.hylexus.xtream.codec.server.reactive.spec;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
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

    /**
     * 同一个 ”网络包“ 的请求和响应，该值应该确保一致（即使是在中途重新包装或修改了 {@link XtreamRequest} 对象）。
     */
    String requestId();

    ByteBuf payload();

    InetSocketAddress remoteAddress();

    Map<String, Object> attributes();

    default void release() {
        XtreamBytes.releaseBuf(this.payload());
    }

    XtreamRequestBuilder mutate();

    interface XtreamRequestBuilder {

        default XtreamRequestBuilder payload(ByteBuf payload) {
            return this.payload(payload, true);
        }

        XtreamRequestBuilder payload(ByteBuf payload, boolean autoRelease);

        XtreamRequestBuilder remoteAddress(InetSocketAddress remoteAddress);

        XtreamRequest build();
    }
}
