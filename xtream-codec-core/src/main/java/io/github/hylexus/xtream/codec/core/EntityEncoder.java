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
import io.github.hylexus.xtream.codec.core.impl.DefaultSerializeContext;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
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
        this.encode(beanMetadata, instance, target);
    }

    public void encode(BeanMetadata beanMetadata, Object instance, ByteBuf target) {
        if (instance == null) {
            return;
        }
        final FieldCodec.SerializeContext context = new DefaultSerializeContext(this, instance);
        for (final BeanPropertyMetadata propertyMetadata : beanMetadata.getPropertyMetadataList()) {
            final Object value = propertyMetadata.getProperty(instance);
            if (value == null) {
                continue;
            }

            if (propertyMetadata.conditionEvaluator().evaluate(context)) {
                propertyMetadata.encodePropertyValue(context, target, value);
            }
        }
    }
}
