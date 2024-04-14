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

package io.github.hylexus.xtream.codec.core.impl.codec;

import io.github.hylexus.xtream.codec.common.utils.BcdOps;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class StringFieldCodec implements FieldCodec<String> {
    protected final String charset;

    private final FieldCodec<String> delegate;

    public StringFieldCodec(String charset) {
        this.charset = charset;
        this.delegate = initDelegateCodec(charset);
    }

    @Override
    public String deserialize(DeserializeContext context, ByteBuf input, int length) {
        return delegate.deserialize(context, input, length);
    }

    @Override
    public void serialize(SerializeContext context, ByteBuf output, String value) {
        delegate.serialize(context, output, value);
    }

    @Override
    public Class<?> underlyingJavaType() {
        return String.class;
    }

    @Override
    public String toString() {
        return "StringFieldCodec{"
                + "charset='" + charset + '\''
                + '}';
    }

    private FieldCodec<String> initDelegateCodec(String charset) {
        if (charset.equalsIgnoreCase("bcd_8421")) {
            return new InternalBcdFieldCodec(charset);
        }
        final Charset nomalCharset = Charset.forName(charset);
        return new InternalSimpleStringFieldCodec(nomalCharset);
    }

    static class InternalSimpleStringFieldCodec extends AbstractFieldCodec<String> {

        private final Charset charset;

        public InternalSimpleStringFieldCodec(Charset charset) {
            this.charset = charset;
        }

        @Override
        public String deserialize(DeserializeContext context, ByteBuf input, int length) {
            return input.readCharSequence(length, charset).toString();
        }

        @Override
        protected void doSerialize(SerializeContext context, ByteBuf output, String value) {
            output.writeCharSequence(value, charset);
        }
    }

    static class InternalBcdFieldCodec extends AbstractFieldCodec<String> {
        protected final String charset;

        public InternalBcdFieldCodec(String charset) {
            this.charset = charset;
        }

        @Override
        public String deserialize(DeserializeContext context, ByteBuf input, int length) {
            return BcdOps.decodeBcd8421AsString(input, length);
        }

        @Override
        protected void doSerialize(SerializeContext context, ByteBuf output, String value) {
            BcdOps.encodeBcd8421StringIntoByteBuf(value, output);
        }

    }
}
