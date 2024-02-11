package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I64FieldCodecLittleEndian extends AbstractFieldCodec<Long> {
    public I64FieldCodecLittleEndian() {
    }

    @Override
    public Long deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readLongLE();
    }

    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Long value) {
        output.writeLongLE(value);
    }
}
