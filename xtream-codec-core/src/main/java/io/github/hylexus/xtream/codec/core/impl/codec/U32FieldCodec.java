package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class U32FieldCodec extends AbstractFieldCodec<Long> {
    public U32FieldCodec() {
    }

    @Override
    public Long deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readUnsignedInt();
    }

    @Override
    protected void doSerialize(FieldSerializeContext context, ByteBuf output, Long value) {
        output.writeInt(value.intValue());
    }
}
