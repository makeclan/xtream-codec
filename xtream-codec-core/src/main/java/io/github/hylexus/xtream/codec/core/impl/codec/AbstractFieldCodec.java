package io.github.hylexus.xtream.codec.core.impl.codec;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;

public abstract class AbstractFieldCodec<T> implements FieldCodec<T> {

    @Override
    public void serialize(SerializeContext context, ByteBuf output, T value) {
        if (value != null) {
            this.doSerialize(context, output, value);
        }
    }

    protected abstract void doSerialize(SerializeContext context, ByteBuf output, T value);
}
