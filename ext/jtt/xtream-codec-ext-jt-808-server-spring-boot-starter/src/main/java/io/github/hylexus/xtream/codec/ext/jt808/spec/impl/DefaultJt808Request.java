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

package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamRequestBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;
import reactor.netty.NettyInbound;

public class DefaultJt808Request extends DefaultXtreamRequest implements Jt808Request {
    protected final Jt808RequestHeader header;
    protected final int originalCheckSum;
    protected final int calculatedCheckSum;

    /**
     * TCP
     */
    public DefaultJt808Request(
            ByteBufAllocator allocator,
            NettyInbound nettyInbound,
            ByteBuf payload,
            Jt808RequestHeader header,
            int originalCheckSum,
            int calculatedCheckSum) {

        super(allocator, nettyInbound, payload);
        this.header = header;
        this.originalCheckSum = originalCheckSum;
        this.calculatedCheckSum = calculatedCheckSum;
    }

    /**
     * UDP
     */
    public DefaultJt808Request(
            ByteBufAllocator allocator,
            NettyInbound nettyInbound,
            DatagramPacket datagramPacket,
            Jt808RequestHeader header,
            int originalCheckSum,
            int calculatedCheckSum) {

        super(allocator, nettyInbound, datagramPacket);
        this.header = header;
        this.originalCheckSum = originalCheckSum;
        this.calculatedCheckSum = calculatedCheckSum;
    }

    @Override
    public Jt808RequestHeader header() {
        return this.header;
    }

    @Override
    public int calculatedCheckSum() {
        return this.calculatedCheckSum;
    }

    @Override
    public int originalCheckSum() {
        return this.originalCheckSum;
    }

    @Override
    public Jt808RequestBuilder mutate() {
        return new DefaultJt808RequestBuilder(this);
    }

    @Override
    public String toString() {
        return "DefaultJt808Request{"
                + "messageId=" + header().messageId() + "(0x" + FormatUtils.toHexString(header.messageId(), 4) + ")"
                + ", header=" + header
                + ", checkSum=" + originalCheckSum
                + '}';
    }

    public static class DefaultJt808RequestBuilder
            extends AbstractXtreamRequestBuilder<Jt808RequestBuilder, Jt808Request>
            implements Jt808RequestBuilder {

        protected Jt808RequestHeader header;
        protected Integer originalCheckSum;
        protected Integer calculatedCheckSum;

        public DefaultJt808RequestBuilder(Jt808Request delegateRequest) {
            super(delegateRequest);
        }

        @Override
        public Jt808RequestBuilder header(Jt808RequestHeader header) {
            this.header = header;
            return this;
        }

        @Override
        public Jt808RequestBuilder calculatedCheckSum(Integer calculatedCheckSum) {
            this.calculatedCheckSum = calculatedCheckSum;
            return this;
        }

        @Override
        public Jt808RequestBuilder originalCheckSum(Integer originalCheckSum) {
            this.originalCheckSum = originalCheckSum;
            return this;
        }

        @Override
        public Jt808Request build() {
            if (this.delegateRequest.type() == Type.TCP) {
                return new DefaultJt808Request(
                        this.delegateRequest.bufferFactory(),
                        this.delegateRequest.underlyingInbound(),
                        this.payload == null ? this.delegateRequest.payload() : this.payload,
                        this.header != null ? this.header : this.delegateRequest.header(),
                        this.originalCheckSum != null ? this.originalCheckSum : this.delegateRequest.originalCheckSum(),
                        this.calculatedCheckSum != null ? this.calculatedCheckSum : this.delegateRequest.calculatedCheckSum()
                );
            }

            return new DefaultJt808Request(
                    this.delegateRequest.bufferFactory(),
                    this.delegateRequest.underlyingInbound(),
                    this.createDatagramPacket(this.payload),
                    this.header != null ? this.header : this.delegateRequest.header(),
                    this.originalCheckSum != null ? this.originalCheckSum : this.delegateRequest.originalCheckSum(),
                    this.calculatedCheckSum != null ? this.calculatedCheckSum : this.delegateRequest.calculatedCheckSum()
            );
        }
    }
}
