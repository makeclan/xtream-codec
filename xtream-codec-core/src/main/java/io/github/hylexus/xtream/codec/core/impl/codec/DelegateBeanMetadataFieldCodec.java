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

package io.github.hylexus.xtream.codec.core.impl.codec;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;

/**
 * 将编解码逻辑委托给上下文中的 {@link io.github.hylexus.xtream.codec.core.EntityDecoder EntityDecoder} 和 {@link io.github.hylexus.xtream.codec.core.EntityDecoder EntityDecoder}
 * <p>
 * 实际上是递归调用 {@link io.github.hylexus.xtream.codec.core.EntityDecoder EntityDecoder} 和 {@link io.github.hylexus.xtream.codec.core.EntityDecoder EntityDecoder}
 *
 * @author hylexus
 * @see io.github.hylexus.xtream.codec.core.EntityDecoder
 * @see io.github.hylexus.xtream.codec.core.EntityEncoder
 * @see io.github.hylexus.xtream.codec.common.bean.impl.MapBeanPropertyMetadata
 * @since 0.0.1
 */
public class DelegateBeanMetadataFieldCodec implements FieldCodec<Object> {
    private final BeanMetadata beanMetadata;

    public DelegateBeanMetadataFieldCodec(BeanMetadata beanMetadata) {
        this.beanMetadata = beanMetadata;
    }

    @Override
    public Object deserialize(DeserializeContext context, ByteBuf input, int length) {
        return context.entityDecoder().decode(beanMetadata, input);
    }

    @Override
    public void serialize(SerializeContext context, ByteBuf output, Object instance) {
        context.entityEncoder().encode(this.beanMetadata, instance, output);
    }
}
