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

    public <T> T decode(T instance, ByteBuf source) {
        return entityDecoder.decode(source, instance);
    }
}
