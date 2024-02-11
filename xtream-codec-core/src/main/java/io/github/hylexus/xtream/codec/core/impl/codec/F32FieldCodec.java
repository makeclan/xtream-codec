package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class F32FieldCodec extends AbstractFieldCodec<Float> {
    public F32FieldCodec() {
    }

    @Override
    public Float deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readFloat();
    }


    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Float value) {
        output.writeFloat(value);
    }
}
