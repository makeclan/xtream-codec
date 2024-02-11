package io.github.hylexus.xtream.codec.core;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.impl.DefaultDeserializeContext;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.netty.buffer.ByteBuf;

public class EntityDecoder {
    private final BeanMetadataRegistry beanMetadataRegistry;

    public EntityDecoder() {
        this(new SimpleBeanMetadataRegistry());
    }

    public EntityDecoder(BeanMetadataRegistry beanMetadataRegistry) {
        this.beanMetadataRegistry = beanMetadataRegistry;
    }

    public <T> T decode(Class<T> entityClass, ByteBuf source) {
        final BeanMetadata beanMetadata = beanMetadataRegistry.getBeanMetadata(entityClass);
        final Object containerInstance = beanMetadata.createNewInstance();
        final FieldCodec.DeserializeContext context = new DefaultDeserializeContext(containerInstance);
        for (final BeanPropertyMetadata propertyMetadata : beanMetadata.getPropertyMetadataList()) {
            final Object fieldValue = propertyMetadata.decodePropertyValue(context, source);
            propertyMetadata.setProperty(containerInstance, fieldValue);
        }
        @SuppressWarnings("unchecked") final T instance1 = (T) containerInstance;
        return instance1;
    }
}
