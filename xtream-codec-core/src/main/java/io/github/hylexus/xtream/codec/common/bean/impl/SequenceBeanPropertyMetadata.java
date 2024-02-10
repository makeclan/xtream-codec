package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SequenceBeanPropertyMetadata extends BasicBeanPropertyMetadata {
    private final NestedBeanPropertyMetadata nestedBeanPropertyMetadata;
    private final BeanPropertyMetadata delegate;

    public SequenceBeanPropertyMetadata(BeanPropertyMetadata delegate, NestedBeanPropertyMetadata metadata) {
        super(delegate.name(), delegate.rawClass(), delegate.field(), delegate.propertyGetter(), delegate.propertySetter());
        this.nestedBeanPropertyMetadata = metadata;
        this.delegate = delegate;
    }

    @Override
    public void encodePropertyValue(FieldCodec.FieldSerializeContext context, ByteBuf output, Object value) {
        final Collection<Object> collection = (Collection<Object>) value;
        for (final Object object : collection) {
            nestedBeanPropertyMetadata.encodePropertyValue(context, output, object);
        }
    }

    @Override
    public Object decodePropertyValue(FieldCodec.DeserializeContext context, ByteBuf input) {
        final int length = delegate.fieldLengthExtractor().extractFieldLength(context, context.evaluationContext());
        final ByteBuf slice = input.readSlice(length);

        final List<Object> list = new ArrayList<>();
        while (slice.isReadable()) {
            final Object value = nestedBeanPropertyMetadata.decodePropertyValue(context, slice);
            list.add(value);
        }
        return list;
    }
}
