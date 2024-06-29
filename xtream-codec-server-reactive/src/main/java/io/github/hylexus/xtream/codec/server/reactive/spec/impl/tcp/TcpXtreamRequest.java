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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamRequest;
import io.netty.buffer.ByteBuf;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
public class TcpXtreamRequest extends AbstractXtreamRequest {

    private final NettyInbound delegate;

    public TcpXtreamRequest(NettyInbound delegate, XtreamSession session, ByteBuf body) {
        super(delegate, session, body);
        this.delegate = delegate;
    }

    @Override
    public InetSocketAddress remoteAddress() {
        final AddrHolder addrHolder = new AddrHolder();
        this.delegate.withConnection(addrHolder);
        return addrHolder.remoteAddress;
    }

    static class AddrHolder implements Consumer<Connection> {
        InetSocketAddress remoteAddress;

        @Override
        public void accept(Connection o) {
            if (this.remoteAddress == null) {
                this.remoteAddress = (InetSocketAddress) o.channel().remoteAddress();
            }
        }
    }

}
