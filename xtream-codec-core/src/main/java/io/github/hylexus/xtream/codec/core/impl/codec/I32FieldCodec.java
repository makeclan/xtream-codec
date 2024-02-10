package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I32FieldCodec extends AbstractFieldCodec<Integer> {
    public I32FieldCodec() {
    }

    @Override
    public Integer deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readInt();
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Integer value) {
        output.writeInt(value);
    }
}
