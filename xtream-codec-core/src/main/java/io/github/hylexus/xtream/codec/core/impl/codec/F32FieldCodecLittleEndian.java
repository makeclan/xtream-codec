package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class F32FieldCodecLittleEndian extends AbstractFieldCodec<Float> {
    public F32FieldCodecLittleEndian() {
    }

    @Override
    public Float deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readFloatLE();
    }


    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Float value) {
        output.writeFloatLE(value);
    }
}
