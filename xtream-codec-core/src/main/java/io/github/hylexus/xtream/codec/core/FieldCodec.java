package io.github.hylexus.xtream.codec.core;

import io.netty.buffer.ByteBuf;
import org.springframework.expression.EvaluationContext;

public interface FieldCodec<T> {

    T deserialize(DeserializeContext context, ByteBuf input, int length);

    void serialize(FieldSerializeContext context, ByteBuf output, T value);

    interface DeserializeContext {

        Object containerInstance();

        EvaluationContext evaluationContext();
    }

    interface FieldSerializeContext {
        Object containerInstance();

        EvaluationContext evaluationContext();

        EntityEncoder entityEncoder();
    }

    class Placeholder implements FieldCodec<Object> {

        @Override
        public Object deserialize(DeserializeContext context, ByteBuf input, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void serialize(FieldSerializeContext context, ByteBuf output, Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
