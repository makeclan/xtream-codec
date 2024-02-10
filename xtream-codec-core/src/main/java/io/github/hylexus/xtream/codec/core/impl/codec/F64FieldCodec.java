package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class F64FieldCodec extends AbstractFieldCodec<Double> {
    public F64FieldCodec() {
    }

    @Override
    public Double deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readDouble();
    }


    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Double value) {
        output.writeDouble(value);
    }
}
