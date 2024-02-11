package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class F64FieldCodecLittleEndian extends AbstractFieldCodec<Double> {
    public F64FieldCodecLittleEndian() {
    }

    @Override
    public Double deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readDoubleLE();
    }


    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Double value) {
        output.writeDoubleLE(value);
    }
}
