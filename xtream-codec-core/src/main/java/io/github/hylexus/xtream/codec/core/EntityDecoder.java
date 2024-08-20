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
