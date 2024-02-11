package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I64FieldCodec extends AbstractFieldCodec<Long> {
    public I64FieldCodec() {
    }

    @Override
    public Long deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readLong();
    }

    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Long value) {
        output.writeLong(value);
    }
}
