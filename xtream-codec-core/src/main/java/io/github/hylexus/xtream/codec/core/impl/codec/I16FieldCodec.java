package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I16FieldCodec extends AbstractFieldCodec<Short> {
    public I16FieldCodec() {
    }

    @Override
    public Short deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readShort();
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Short value) {
        output.writeShort(value);
    }
}
