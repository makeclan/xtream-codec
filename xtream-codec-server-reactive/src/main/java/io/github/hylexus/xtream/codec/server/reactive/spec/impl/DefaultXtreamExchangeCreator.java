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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;

public class DefaultXtreamExchangeCreator implements XtreamExchangeCreator {
    @SuppressWarnings("rawtypes")
    protected final XtreamSessionManager sessionManager;

    public DefaultXtreamExchangeCreator(@SuppressWarnings("rawtypes") XtreamSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public XtreamExchange createTcpExchange(ByteBufAllocator allocator, NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf byteBuf) {
        final XtreamRequest.Type type = XtreamRequest.Type.TCP;
        final DefaultXtreamRequest request = new DefaultXtreamRequest(this.generateRequestId(nettyInbound), allocator, nettyInbound, byteBuf);
        final InetSocketAddress remoteAddress = request.remoteAddress();
        final DefaultXtreamResponse response = new DefaultXtreamResponse(allocator, nettyOutbound, type, remoteAddress);

        return new DefaultXtreamExchange(sessionManager, request, response);
    }

    @Override
    public XtreamExchange createUdpExchange(ByteBufAllocator allocator, NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf payload, InetSocketAddress remoteAddress) {
        final XtreamRequest.Type type = XtreamRequest.Type.UDP;
        final DefaultXtreamRequest request = new DefaultXtreamRequest(this.generateRequestId(nettyInbound), allocator, nettyInbound, payload, remoteAddress);
        final DefaultXtreamResponse response = new DefaultXtreamResponse(allocator, nettyOutbound, type, remoteAddress);

        return new DefaultXtreamExchange(sessionManager, request, response);
    }
}
