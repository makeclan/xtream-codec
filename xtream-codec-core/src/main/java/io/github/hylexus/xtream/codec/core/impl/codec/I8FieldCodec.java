package io.github.hylexus.xtream.codec.core.impl.codec;

import io.netty.buffer.ByteBuf;

public class I8FieldCodec extends AbstractFieldCodec<Byte> {
    public I8FieldCodec() {
    }

    @Override
    public Byte deserialize(DeserializeContext context, ByteBuf input, int length) {
        return input.readByte();
    }

    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Byte value) {
        output.writeByte(value);
    }
}
