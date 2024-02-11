package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I16FieldCodecLittleEndian extends AbstractFieldCodec<Short> {
    public I16FieldCodecLittleEndian() {
    }

    @Override
    public Short deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readShortLE();
    }

    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Short value) {
        output.writeShortLE(value);
    }
}
