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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.handler;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.DefaultTcpXtreamNettyHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;

/**
 * @author hylexus
 */
public class Jt808AttachmentServerTcpHandlerAdapter extends DefaultTcpXtreamNettyHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Jt808AttachmentServerTcpHandlerAdapter.class);

    public Jt808AttachmentServerTcpHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator xtreamExchangeCreator, XtreamHandler xtreamHandler) {
        super(allocator, xtreamExchangeCreator, xtreamHandler);
    }

    @Override
    protected Mono<Void> handleSingleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, ByteBuf payload, InetSocketAddress remoteAddress) {
        // 普通的指令消息
        if (!JtProtocolUtils.isAttachmentRequest(payload)) {
            return super.handleSingleRequest(nettyInbound, nettyOutbound, payload, remoteAddress);
        }
        // 码流消息
        log.info("===> {}", FormatUtils.toHexString(payload));
        return Mono.empty();
    }
}
