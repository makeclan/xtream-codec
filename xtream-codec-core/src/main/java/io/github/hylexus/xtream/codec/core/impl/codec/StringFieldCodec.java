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
    public void serialize(FieldSerializeContext context, ByteBuf output, String value) {
        delegate.serialize(context, output, value);
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
        protected void doSerialize(FieldSerializeContext context, ByteBuf output, String value) {
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
            return BcdOps.encodeBcd8421(input, length);
        }

        @Override
        protected void doSerialize(FieldSerializeContext context, ByteBuf output, String value) {
            final byte[] bytes = BcdOps.decodeBcd8421(value);
            output.writeBytes(bytes);
        }

    }
}
