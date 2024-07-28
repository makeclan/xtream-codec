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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamRequestBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

public class DefaultUdpXtreamRequestBuilder extends AbstractXtreamRequestBuilder {

    public DefaultUdpXtreamRequestBuilder(XtreamRequest delegate) {
        super(delegate);
    }

    @Override
    public XtreamRequest build() {
        return new UdpXtreamRequest(
                delegate.bufferFactory(),
                delegate.underlyingInbound(),
                sessionMono != null ? sessionMono : delegate.session(),
                this.createDatagramPacket(this.payload)
        );
    }

    protected DatagramPacket createDatagramPacket(ByteBuf payload) {
        return new DatagramPacket(
                payload != null ? payload : delegate.payload(),
                null,
                remoteAddress != null ? remoteAddress : delegate.remoteAddress()
        );
    }
}
