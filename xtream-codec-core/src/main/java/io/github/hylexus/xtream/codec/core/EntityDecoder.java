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

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.impl.DefaultDeserializeContext;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.netty.buffer.ByteBuf;

public class EntityDecoder {
    protected final BeanMetadataRegistry beanMetadataRegistry;

    public EntityDecoder() {
        this(new SimpleBeanMetadataRegistry());
    }

    public EntityDecoder(BeanMetadataRegistry beanMetadataRegistry) {
        this.beanMetadataRegistry = beanMetadataRegistry;
    }

    public <T> T decode(Class<T> entityClass, ByteBuf source) {
        final BeanMetadata beanMetadata = beanMetadataRegistry.getBeanMetadata(entityClass);
        final Object containerInstance = beanMetadata.createNewInstance();
        return this.decode(source, beanMetadata, containerInstance);
    }

    public <T> T decode(BeanMetadata beanMetadata, ByteBuf source) {
        final Object containerInstance = beanMetadata.createNewInstance();
        return decode(source, beanMetadata, containerInstance);
    }

    public <T> T decode(ByteBuf source, Object containerInstance) {
        final BeanMetadata beanMetadata = beanMetadataRegistry.getBeanMetadata(containerInstance.getClass());
        return decode(source, beanMetadata, containerInstance);
    }

    public <T> T decode(ByteBuf source, BeanMetadata beanMetadata, Object containerInstance) {
        final FieldCodec.DeserializeContext context = new DefaultDeserializeContext(this, containerInstance);
        for (final BeanPropertyMetadata propertyMetadata : beanMetadata.getPropertyMetadataList()) {
            if (propertyMetadata.conditionEvaluator().evaluate(context)) {
                final Object fieldValue = propertyMetadata.decodePropertyValue(context, source);
                propertyMetadata.setProperty(containerInstance, fieldValue);
            }
        }
        @SuppressWarnings("unchecked") final T instance1 = (T) containerInstance;
        return instance1;
    }
}
