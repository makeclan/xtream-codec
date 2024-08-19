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

package io.github.hylexus.xtream.codec.core.annotation;

import io.netty.buffer.ByteBuf;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XtreamRequestBody {

    /**
     * 仅仅适用于 {@link ByteBuf} 类型
     *
     * @return {@code true}: 如果目标对象是 {@link ByteBuf} 类型; 返回 {@link ByteBuf#slice()} 而不是 {@link ByteBuf} 本身
     */
    boolean bufferAsSlice() default true;

}
