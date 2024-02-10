package io.github.hylexus.xtream.codec.core.impl.codec;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.netty.buffer.ByteBuf;

public class ByteBoxArrayFieldCodec extends AbstractFieldCodec<Byte[]> {
    public ByteBoxArrayFieldCodec() {
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Byte[] value) {
        for (final Byte b : value) {
            output.writeByte(b);
        }
    }

    @Override
    public Byte[] deserialize(DeserializeContext context, ByteBuf input, int length) {
        final byte[] bytes = XtreamBytes.readBytes(input, length);
        final Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }
}
