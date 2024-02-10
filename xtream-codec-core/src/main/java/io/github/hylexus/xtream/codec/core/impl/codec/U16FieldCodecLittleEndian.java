package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class U16FieldCodecLittleEndian extends AbstractFieldCodec<Integer> {
    public U16FieldCodecLittleEndian() {
    }

    @Override
    public Integer deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readUnsignedShortLE();
    }


    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Integer value) {
        output.writeShortLE(value);
    }
}
