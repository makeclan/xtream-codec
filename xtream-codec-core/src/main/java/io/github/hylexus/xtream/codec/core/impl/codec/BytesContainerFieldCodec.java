/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.core.impl.codec;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.core.type.ByteArrayContainer;
import io.github.hylexus.xtream.codec.core.type.BytesContainer;
import io.netty.buffer.ByteBuf;

public class BytesContainerFieldCodec extends AbstractFieldCodec<BytesContainer> {
    public static final BytesContainerFieldCodec INSTANCE = new BytesContainerFieldCodec();

    private BytesContainerFieldCodec() {
    }

    @Override
    protected void doSerialize(BeanPropertyMetadata propertyMetadata, SerializeContext context, ByteBuf output, BytesContainer value) {
        // todo 优化：直接写入 BytesContainer
        final byte[] bytes = value.asBytes();
        output.writeBytes(bytes);
    }

    @Override
    public BytesContainer deserialize(BeanPropertyMetadata propertyMetadata, DeserializeContext context, ByteBuf input, int length) {
        final byte[] bytes = XtreamBytes.readBytes(input, length);
        return ByteArrayContainer.ofBytes(bytes);
    }
}
