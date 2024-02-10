package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class U8FieldCodec extends AbstractFieldCodec<Short> {
    public U8FieldCodec() {
    }

    @Override
    public Short deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readUnsignedByte();
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Short value) {
        output.writeByte(value);
    }
}
