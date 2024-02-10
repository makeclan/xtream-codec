package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I32FieldCodecLittleEndian extends AbstractFieldCodec<Integer> {
    public I32FieldCodecLittleEndian() {
    }

    @Override
    public Integer deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readIntLE();
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Integer value) {
        output.writeIntLE(value);
    }
}
