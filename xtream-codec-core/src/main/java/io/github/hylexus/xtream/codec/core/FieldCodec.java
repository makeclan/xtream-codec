/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.core;

import io.netty.buffer.ByteBuf;
import org.springframework.expression.EvaluationContext;

public interface FieldCodec<T> {

    T deserialize(DeserializeContext context, ByteBuf input, int length);

    void serialize(SerializeContext context, ByteBuf output, T value);

    interface CodecContext {

        Object containerInstance();

        EvaluationContext evaluationContext();

    }

    interface DeserializeContext extends CodecContext {
    }

    interface SerializeContext extends CodecContext {

        EntityEncoder entityEncoder();

    }

    class Placeholder implements FieldCodec<Object> {

        @Override
        public Object deserialize(DeserializeContext context, ByteBuf input, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void serialize(SerializeContext context, ByteBuf output, Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
