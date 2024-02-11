package io.github.hylexus.xtream.codec.core.impl.codec;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.netty.buffer.ByteBuf;

public class ByteArrayFieldCodec extends AbstractFieldCodec<byte[]> {
    public ByteArrayFieldCodec() {
    }

    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, byte[] value) {
        output.writeBytes(value);
    }

    @Override
    public byte[] deserialize(DeserializeContext context, ByteBuf input, int length) {
        return XtreamBytes.readBytes(input, length);
    }
}
