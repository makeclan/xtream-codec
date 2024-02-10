package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class U16FieldCodec extends AbstractFieldCodec<Integer> {
    public U16FieldCodec() {
    }

    @Override
    public Integer deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readUnsignedShort();
    }


    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Integer value) {
        output.writeShort(value);
    }
}
