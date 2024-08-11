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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

public class DefaultXtreamRequestBuilder implements XtreamRequest.XtreamRequestBuilder {

    protected final XtreamRequest delegate;

    protected ByteBuf payload;
    protected InetSocketAddress remoteAddress;
    protected Mono<XtreamSession> sessionMono;

    public DefaultXtreamRequestBuilder(XtreamRequest delegate) {
        this.delegate = delegate;
        this.remoteAddress = delegate.remoteAddress();
    }

    @Override
    public XtreamRequest.XtreamRequestBuilder payload(ByteBuf payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public XtreamRequest.XtreamRequestBuilder remoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    @Override
    public XtreamRequest.XtreamRequestBuilder session(Mono<XtreamSession> sessionMono) {
        this.sessionMono = sessionMono;
        return this;
    }

    public XtreamRequest build() {
        if (this.delegate.type() == XtreamRequest.Type.TCP) {
            return new DefaultXtreamRequest(
                    this.delegate.bufferFactory(),
                    this.delegate.underlyingInbound(),
                    this.payload != null ? this.payload : this.delegate.payload()
            );
        }
        return new DefaultXtreamRequest(
                this.delegate.bufferFactory(),
                this.delegate.underlyingInbound(),
                this.createDatagramPacket(this.payload)
        );
    }

    protected DatagramPacket createDatagramPacket(ByteBuf payload) {
        return new DatagramPacket(
                payload != null ? payload : this.delegate.payload(),
                null,
                this.remoteAddress != null ? this.remoteAddress : this.delegate.remoteAddress()
        );
    }
}
