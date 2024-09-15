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
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808UdpDatagramPackageSplitter;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeCreator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

/**
 * @author hylexus
 */
public class Jt808AttachmentServerUdpHandlerAdapter extends Jt808InstructionServerUdpHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Jt808AttachmentServerUdpHandlerAdapter.class);

    public Jt808AttachmentServerUdpHandlerAdapter(ByteBufAllocator allocator, XtreamExchangeCreator exchangeCreator, XtreamHandler xtreamHandler, Jt808UdpDatagramPackageSplitter splitter) {
        super(allocator, exchangeCreator, xtreamHandler, splitter);
    }

    @Override
    protected Mono<Void> handleRequest(NettyInbound nettyInbound, NettyOutbound nettyOutbound, DatagramPacket datagramPacket) {
        if (!JtProtocolUtils.isAttachmentRequest(datagramPacket.content())) {
            return super.handleRequest(nettyInbound, nettyOutbound, datagramPacket);
        }
        log.info("===> {}", FormatUtils.toHexString(datagramPacket.content()));
        return Mono.empty();
    }
}
