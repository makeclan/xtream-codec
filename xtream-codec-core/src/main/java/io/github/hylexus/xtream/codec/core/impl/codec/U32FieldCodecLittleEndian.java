package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class U32FieldCodecLittleEndian extends AbstractFieldCodec<Long> {
    public U32FieldCodecLittleEndian() {
    }

    @Override
    public Long deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readUnsignedIntLE();
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Long value) {
        output.writeIntLE(value.intValue());
    }
}
