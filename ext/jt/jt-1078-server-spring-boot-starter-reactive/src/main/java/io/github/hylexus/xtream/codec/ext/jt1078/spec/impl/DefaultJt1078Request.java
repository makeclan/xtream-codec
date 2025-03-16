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

package io.github.hylexus.xtream.codec.ext.jt1078.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamRequestBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;

/**
 * @author hylexus
 */
public class DefaultJt1078Request extends DefaultXtreamRequest implements Jt1078Request {
    protected final String traceId;
    private final Jt1078RequestHeader header;

    public DefaultJt1078Request(
            String requestId, String traceId, ByteBufAllocator allocator, NettyInbound nettyInbound, Type type, InetSocketAddress remoteAddress,
            Jt1078RequestHeader header, ByteBuf payload) {
        super(requestId, allocator, nettyInbound, type, payload, remoteAddress);
        this.header = header;
        this.traceId = traceId;
    }

    @Override
    public String traceId() {
        return this.traceId;
    }

    @Override
    public Jt1078RequestHeader header() {
        return this.header;
    }

    @Override
    public Jt1078RequestBuilder mutate() {
        return new DefaultJt1078RequestBuilder(this);
    }

    @Override
    public String toString() {
        return "DefaultJt1078Request{" + "header=" + header + '}';
    }

    public static class DefaultJt1078RequestBuilder
            extends AbstractXtreamRequestBuilder<Jt1078RequestBuilder, Jt1078Request>
            implements Jt1078RequestBuilder {
        protected String traceId;
        protected Jt1078RequestHeader header;

        public DefaultJt1078RequestBuilder(Jt1078Request delegateRequest) {
            super(delegateRequest);
            this.traceId = delegateRequest.traceId();
        }

        @Override
        public Jt1078RequestBuilder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        @Override
        public Jt1078RequestBuilder header(Jt1078RequestHeader header) {
            this.header = header;
            return this;
        }

        @Override
        public Jt1078Request build() {
            return new DefaultJt1078Request(
                    this.delegateRequest.requestId(),
                    this.traceId,
                    this.delegateRequest.bufferFactory(),
                    this.delegateRequest.underlyingInbound(),
                    this.delegateRequest.type(),
                    this.remoteAddress != null ? this.remoteAddress : this.delegateRequest.remoteAddress(),
                    this.header != null ? this.header : this.delegateRequest.header(),
                    this.payload == null ? this.delegateRequest.payload() : this.payload
            );
        }
    }
}
