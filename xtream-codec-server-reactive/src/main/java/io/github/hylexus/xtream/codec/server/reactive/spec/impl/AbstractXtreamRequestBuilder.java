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

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

public abstract class AbstractXtreamRequestBuilder<B extends XtreamRequest.XtreamRequestBuilder, R extends XtreamRequest>
        implements XtreamRequest.XtreamRequestBuilder {

    protected final R delegateRequest;

    protected ByteBuf payload;
    protected InetSocketAddress remoteAddress;

    public AbstractXtreamRequestBuilder(R delegateRequest) {
        this.delegateRequest = delegateRequest;
        this.payload = delegateRequest.payload();
        this.remoteAddress = delegateRequest.remoteAddress();
    }

    @SuppressWarnings("unchecked")
    B self() {
        return (B) this;
    }

    @Override
    public B payload(ByteBuf payload, boolean autoRelease) {
        final ByteBuf old = this.payload;
        try {
            this.payload = payload;
            return self();
        } finally {
            if (autoRelease) {
                XtreamBytes.releaseBuf(old);
            }
        }
    }

    @Override
    public B remoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return self();
    }

    public abstract R build();

    protected DatagramPacket createDatagramPacket(ByteBuf payload) {
        return new DatagramPacket(
                payload != null ? payload : this.delegateRequest.payload(),
                null,
                this.remoteAddress != null ? this.remoteAddress : this.delegateRequest.remoteAddress()
        );
    }
}
