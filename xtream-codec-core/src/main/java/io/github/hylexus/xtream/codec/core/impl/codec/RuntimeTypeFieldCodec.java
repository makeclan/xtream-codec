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

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.utils.XtreamTypes;
import io.github.hylexus.xtream.codec.core.EntityEncoder;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.netty.buffer.ByteBuf;
import org.springframework.util.ClassUtils;

import java.util.Optional;

/**
 * 该实现类针对于 具体类型只有在运行时才能确定的场景
 *
 * @author hylexus
 * @see io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata.FiledDataType#dynamic
 * @see io.github.hylexus.xtream.codec.core.type.Preset.RustStyle.dyn
 * @see io.github.hylexus.xtream.codec.core.type.Preset.JtStyle.RuntimeType
 */
public class RuntimeTypeFieldCodec extends AbstractFieldCodec<Object> {
    public static final RuntimeTypeFieldCodec INSTANCE = new RuntimeTypeFieldCodec();

    private RuntimeTypeFieldCodec() {
    }

    @Override
    public Object deserialize(BeanPropertyMetadata propertyMetadata, DeserializeContext context, ByteBuf input, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doSerialize(BeanPropertyMetadata propertyMetadata, SerializeContext context, ByteBuf output, Object value) {
        final XtreamField xtreamField = propertyMetadata.findAnnotation(XtreamField.class)
                .orElseThrow();
        final Class<?> targetClass = getTargetClass(value);

        final EntityEncoder entityEncoder = context.entityEncoder();
        final int sizeInBytes = xtreamField.length() > 0 ? xtreamField.length() : XtreamTypes.getDefaultSizeInBytes(targetClass).orElse(-1);
        final Optional<FieldCodec<?>> fieldCodec = entityEncoder.getFieldCodecRegistry().getFieldCodec(targetClass, sizeInBytes, xtreamField.charset(), xtreamField.littleEndian());

        if (fieldCodec.isPresent()) {
            @SuppressWarnings("unchecked") final FieldCodec<Object> codec = (FieldCodec<Object>) fieldCodec.get();
            codec.serialize(propertyMetadata, context, output, value);
        } else {
            final BeanMetadata beanMetadata = entityEncoder.getBeanMetadataRegistry().getBeanMetadata(targetClass);
            entityEncoder.encode(beanMetadata, value, output);
        }
    }

    protected Class<?> getTargetClass(Object value) {
        final Class<?> cls = value.getClass();
        if (ClassUtils.isLambdaClass(cls)) {
            return cls.getInterfaces()[0];
        }
        return cls;
    }

}
