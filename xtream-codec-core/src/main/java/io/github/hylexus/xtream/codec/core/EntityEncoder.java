package io.github.hylexus.xtream.codec.core;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.utils.SimpleBeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.impl.DefaultFieldSerializeContext;
import io.netty.buffer.ByteBuf;

public class EntityEncoder {

    private final BeanMetadataRegistry beanMetadataRegistry;

    public EntityEncoder() {
        this(new SimpleBeanMetadataRegistry());
    }

    public EntityEncoder(BeanMetadataRegistry beanMetadataRegistry) {
        this.beanMetadataRegistry = beanMetadataRegistry;
    }

    public void encode(Object instance, ByteBuf target) {
        if (instance == null) {
            return;
        }
        final BeanMetadata beanMetadata = beanMetadataRegistry.getBeanMetadata(instance.getClass());
        final FieldCodec.FieldSerializeContext context = new DefaultFieldSerializeContext(this, instance);
        for (final BeanPropertyMetadata propertyMetadata : beanMetadata.getPropertyMetadataList()) {
            final Object value = propertyMetadata.getProperty(instance);
            if (value == null) {
                continue;
            }

            if (propertyMetadata.conditionEvaluator().evaluateConditionalProperty(context)) {
                propertyMetadata.encodePropertyValue(context, target, value);
            }
        }
    }
}
