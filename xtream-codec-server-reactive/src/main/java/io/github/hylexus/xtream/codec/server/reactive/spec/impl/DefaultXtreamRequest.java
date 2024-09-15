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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
public class DefaultXtreamRequest implements XtreamRequest {
    protected final ByteBufAllocator allocator;
    protected final NettyInbound delegate;
    protected final ByteBuf payload;
    protected String requestId;
    protected final Type type;
    protected InetSocketAddress remoteAddress;

    /**
     * TCP
     */
    public DefaultXtreamRequest(String requestId, ByteBufAllocator allocator, NettyInbound delegate, ByteBuf payload) {
        this.requestId = requestId;
        this.allocator = allocator;
        this.delegate = delegate;
        this.payload = payload;
        this.type = Type.TCP;
        this.remoteAddress = this.initTcpRemoteAddress(delegate);
    }

    /**
     * UDP
     */
    public DefaultXtreamRequest(String requestId, ByteBufAllocator allocator, NettyInbound delegate, ByteBuf payload, InetSocketAddress remoteAddress) {
        this.requestId = requestId;
        this.allocator = allocator;
        this.delegate = delegate;
        // this.payload = datagramPacket.content();
        this.payload = payload;
        this.type = Type.UDP;
        // this.remoteAddress = datagramPacket.sender();
        this.remoteAddress = remoteAddress;
    }

    @Override
    public Type type() {
        return this.type;
    }

    @Override
    public ByteBufAllocator bufferFactory() {
        return this.allocator;
    }

    @Override
    public NettyInbound underlyingInbound() {
        return this.delegate;
    }

    @Override
    public String requestId() {
        return this.requestId;
    }

    @Override
    public ByteBuf payload() {
        return this.payload;
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return this.remoteAddress;
    }

    @Override
    public Map<String, Object> attributes() {
        return Map.of();
    }

    @Override
    public XtreamRequestBuilder mutate() {
        return new DefaultXtreamRequestBuilder(this);
    }

    @Override
    public String toString() {
        return "DefaultXtreamRequest{"
                + "requestId='" + requestId() + '\''
                + ", payload='" + (payload.refCnt() > 0 ? FormatUtils.toHexString(payload) : "<FREED>") + '\''
                + '}';
    }

    protected InetSocketAddress initTcpRemoteAddress(NettyInbound delegate) {
        final AddrHolder addrHolder = new AddrHolder();
        delegate.withConnection(addrHolder);
        return addrHolder.remoteAddress;
    }

    protected static class AddrHolder implements Consumer<Connection> {
        InetSocketAddress remoteAddress;

        @Override
        public void accept(Connection o) {
            if (this.remoteAddress == null) {
                this.remoteAddress = (InetSocketAddress) o.channel().remoteAddress();
            }
        }
    }
}
