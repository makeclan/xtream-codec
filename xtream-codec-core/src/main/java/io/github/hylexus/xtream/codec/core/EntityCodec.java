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

import io.github.hylexus.xtream.codec.core.impl.DefaultFieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.netty.buffer.ByteBuf;

public class EntityCodec {
    private final EntityEncoder entityEncoder;
    private final EntityDecoder entityDecoder;
    public static EntityCodec DEFAULT = new EntityCodec(
            new SimpleBeanMetadataRegistry(
                    new DefaultFieldCodecRegistry(),
                    new XtreamCacheableClassPredicate.Default()
            )
    );

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
