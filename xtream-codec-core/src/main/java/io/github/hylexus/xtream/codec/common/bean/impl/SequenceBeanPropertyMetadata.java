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

package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.ContainerInstanceFactory;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.utils.BeanUtils;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

public class SequenceBeanPropertyMetadata extends BasicBeanPropertyMetadata {
    private final NestedBeanPropertyMetadata nestedBeanPropertyMetadata;
    private final BeanPropertyMetadata delegate;
    private final ContainerInstanceFactory containerInstanceFactory;

    public SequenceBeanPropertyMetadata(BeanPropertyMetadata delegate, NestedBeanPropertyMetadata metadata) {
        super(delegate.name(), delegate.rawClass(), delegate.field(), delegate.propertyGetter(), delegate.propertySetter());
        this.nestedBeanPropertyMetadata = metadata;
        this.delegate = delegate;
        this.containerInstanceFactory = this.xtreamField.containerInstanceFactory() == ContainerInstanceFactory.PlaceholderContainerInstanceFactory.class
                ? BeanUtils.createNewInstance(ContainerInstanceFactory.ArrayListContainerInstanceFactory.class, (Object[]) null)
                : BeanUtils.createNewInstance(this.xtreamField.containerInstanceFactory(), (Object[]) null);
    }

    @Override
    public ContainerInstanceFactory containerInstanceFactory() {
        return this.containerInstanceFactory;
    }

    @Override
    public void encodePropertyValue(FieldCodec.SerializeContext context, ByteBuf output, Object value) {
        @SuppressWarnings("unchecked") final Collection<Object> collection = (Collection<Object>) value;
        for (final Object object : collection) {
            nestedBeanPropertyMetadata.encodePropertyValue(context, output, object);
        }
    }

    @Override
    public Object decodePropertyValue(FieldCodec.DeserializeContext context, ByteBuf input) {
        final int length = delegate.fieldLengthExtractor().extractFieldLength(context, context.evaluationContext());

        final ByteBuf slice = length < 0
                ? input // all remaining
                : input.readSlice(length);

        @SuppressWarnings("unchecked") final Collection<Object> list = (Collection<Object>) this.containerInstanceFactory().create();
        while (slice.isReadable()) {
            final Object value = nestedBeanPropertyMetadata.decodePropertyValue(context, slice);
            list.add(value);
        }
        return list;
    }
}
