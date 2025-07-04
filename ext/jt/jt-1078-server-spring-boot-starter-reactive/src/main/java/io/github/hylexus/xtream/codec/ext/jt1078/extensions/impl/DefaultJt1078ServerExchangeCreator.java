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

package io.github.hylexus.xtream.codec.ext.jt1078.extensions.impl;

import io.github.hylexus.xtream.codec.ext.jt1078.codec.Jt1078RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt1078.extensions.Jt1078ServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamExchangeCreator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;

public class DefaultJt1078ServerExchangeCreator
        extends DefaultXtreamExchangeCreator
        implements Jt1078ServerExchangeCreator {

    protected final Jt1078RequestDecoder requestDecoder;

    public DefaultJt1078ServerExchangeCreator(Jt1078SessionManager sessionManager, Jt1078RequestDecoder requestDecoder) {
        super(sessionManager);
        this.requestDecoder = requestDecoder;
    }

    @Override
    protected XtreamRequest doCreateTcpRequest(ByteBufAllocator allocator, NettyInbound nettyInbound, ByteBuf byteBuf, InetSocketAddress remoteAddress, XtreamRequest.Type type) {
        return this.requestDecoder.decode(false, this.generateRequestId(nettyInbound), allocator, nettyInbound, XtreamInbound.Type.TCP, remoteAddress, byteBuf);
    }

    @Override
    protected XtreamRequest doCreateUdpRequest(ByteBufAllocator allocator, NettyInbound nettyInbound, ByteBuf payload, InetSocketAddress remoteAddress, XtreamRequest.Type type) {
        return this.requestDecoder.decode(false, this.generateRequestId(nettyInbound), allocator, nettyInbound, XtreamInbound.Type.UDP, remoteAddress, payload);
    }

}
