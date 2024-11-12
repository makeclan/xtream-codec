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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.impl;

import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808AttachmentServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808AttachmentSessionManager;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ServerType;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamExchangeCreator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;

public class BuiltinJt808AttachmentServerExchangeCreator
        extends DefaultXtreamExchangeCreator
        implements Jt808AttachmentServerExchangeCreator {

    protected final Jt808RequestDecoder requestDecoder;
    protected final Jt808RequestLifecycleListener requestLifecycleListener;

    public BuiltinJt808AttachmentServerExchangeCreator(Jt808AttachmentSessionManager sessionManager, Jt808RequestDecoder requestDecoder, Jt808RequestLifecycleListener requestLifecycleListener) {
        super(sessionManager);
        this.requestDecoder = requestDecoder;
        this.requestLifecycleListener = requestLifecycleListener;
    }

    @Override
    protected XtreamRequest doCreateTcpRequest(ByteBufAllocator allocator, NettyInbound nettyInbound, ByteBuf byteBuf, InetSocketAddress remoteAddress, XtreamRequest.Type type) {
        final Jt808Request request = this.requestDecoder.decode(Jt808ServerType.ATTACHMENT_SERVER, this.generateRequestId(nettyInbound), allocator, nettyInbound, XtreamInbound.Type.TCP, byteBuf, remoteAddress);
        this.requestLifecycleListener.afterRequestDecode(nettyInbound, byteBuf, request);
        return request;
    }

    @Override
    protected XtreamRequest doCreateUdpRequest(ByteBufAllocator allocator, NettyInbound nettyInbound, ByteBuf payload, InetSocketAddress remoteAddress, XtreamRequest.Type type) {
        final Jt808Request request = this.requestDecoder.decode(Jt808ServerType.ATTACHMENT_SERVER, this.generateRequestId(nettyInbound), allocator, nettyInbound, XtreamInbound.Type.UDP, payload, remoteAddress);
        this.requestLifecycleListener.afterRequestDecode(nettyInbound, payload, request);
        return request;
    }
}
