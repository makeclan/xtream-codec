package io.github.hylexus.xtream.codec.core;

import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.netty.buffer.ByteBuf;

public class EntityCodec {
    private final EntityEncoder entityEncoder;
    private final EntityDecoder entityDecoder;

    public EntityCodec() {
        this(new SimpleBeanMetadataRegistry());
    }

    public EntityCodec(BeanMetadataRegistry beanMetadataRegistry) {
        this.entityEncoder = new EntityEncoder(beanMetadataRegistry);
        this.entityDecoder = new EntityDecoder(beanMetadataRegistry);
    }

    public void encode(Object instance, ByteBuf target) {
        entityEncoder.encode(instance, target);
    }

    public <T> T decode(Class<T> entityClass, ByteBuf source) {
        return entityDecoder.decode(entityClass, source);
    }
}
