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

package io.github.hylexus.xtream.debug.codec.core.demo04;

import io.github.hylexus.xtream.codec.core.BeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.netty.buffer.ByteBuf;

public class SimpleSubPackageEntityCodec extends EntityCodec {

    final SimpleSubPackageEntityDecoder decoder;

    public SimpleSubPackageEntityCodec() {
        this(new SimpleBeanMetadataRegistry());
    }

    public SimpleSubPackageEntityCodec(BeanMetadataRegistry beanMetadataRegistry) {
        this.decoder = new SimpleSubPackageEntityDecoder(beanMetadataRegistry);
    }

    @Override
    public <T> T decode(T instance, ByteBuf source) {
        return this.decoder.decode(source, instance);
    }

    @Override
    public <T> T decode(Class<T> entityClass, ByteBuf source) {
        return this.decoder.decode(entityClass, source);
    }

    @Override
    public void encode(Object instance, ByteBuf target) {
        // todo 实现支持分包的编码逻辑...
        super.encode(instance, target);
    }
}
