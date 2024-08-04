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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
public class DefaultXtreamRequest implements XtreamRequest {
    protected final ByteBufAllocator allocator;
    protected final NettyInbound delegate;
    protected final Mono<XtreamSession> sessionMono;
    protected final ByteBuf payload;
    protected String id;
    protected final Type type;
    protected InetSocketAddress remoteAddress;

    /**
     * TCP
     */
    public DefaultXtreamRequest(ByteBufAllocator allocator, NettyInbound delegate, Mono<XtreamSession> session, ByteBuf payload) {
        this.allocator = allocator;
        this.delegate = delegate;
        this.sessionMono = session;
        this.payload = payload;
        this.type = Type.TCP;
        this.remoteAddress = this.initTcpRemoteAddress(delegate);
    }

    /**
     * UDP
     */
    public DefaultXtreamRequest(ByteBufAllocator allocator, NettyInbound delegate, Mono<XtreamSession> session, DatagramPacket datagramPacket) {
        this.allocator = allocator;
        this.delegate = delegate;
        this.sessionMono = session;
        this.payload = datagramPacket.content();
        this.type = Type.UDP;
        this.remoteAddress = datagramPacket.sender();
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
    public String logId() {
        if (this.id == null) {
            this.id = initId();
        }
        return this.id;
    }

    protected String initId() {
        return UUID.randomUUID().toString().replace("-", "");
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
    public Mono<XtreamSession> session() {
        return this.sessionMono;
    }

    @Override
    public XtreamRequestBuilder mutate() {
        return new DefaultXtreamRequestBuilder(this);
    }

    @Override
    public String toString() {
        return "DefaultXtreamRequest{"
                + "id='" + id + '\''
                + ", payload='" + FormatUtils.toHexString(payload) + '\''
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
