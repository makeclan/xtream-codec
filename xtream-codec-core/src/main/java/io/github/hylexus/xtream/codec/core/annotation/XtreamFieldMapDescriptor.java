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

import io.github.hylexus.xtream.codec.core.FieldCodec;
import lombok.Getter;

import java.lang.annotation.*;

/**
 * @author hylexus
 * @see FieldCodec
 * @see io.github.hylexus.xtream.codec.core.FieldCodecRegistry
 * @see io.github.hylexus.xtream.codec.core.EntityCodec
 * @see io.github.hylexus.xtream.codec.core.type.Preset.RustStyle
 * @see io.github.hylexus.xtream.codec.core.type.Preset.JtStyle
 * @see <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">SpEL 官方文档</a>
 * @see <a href="https://stackoverflow.com/questions/5001172/java-reflection-getting-fields-and-methods-in-declaration-order">java-reflection-getting-fields-and-methods-in-declaration-order</a>
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XtreamFieldMapDescriptor {

    KeyDescriptor keyDescriptor();

    DefaultValueDescriptor defaultValueDescriptor();

    ValueDescriptor[] valueDescriptors() default {};

    @Getter
    enum KeyType {
        i8(Byte.class, 1),
        u8(Short.class, 1),
        i16(Short.class, 2),
        u16(Integer.class, 2),
        i32(Integer.class, 4),
        u32(Long.class, 4),
        str(String.class, -1),
        ;

        private final Class<?> javaType;
        private final int sizeInBytes;

        KeyType(Class<?> javaType, int sizeInBytes) {
            this.javaType = javaType;
            this.sizeInBytes = sizeInBytes;
        }
    }

    @interface KeyDescriptor {

        KeyType type();

        boolean littleEndian() default false;

        // todo delete
        int length() default -1;

        String charset() default "UTF-8";
    }

    @interface DefaultValueDescriptor {

        int defaultValueFieldLengthSize();

        boolean defaultValueFieldLengthSizeIsLittleEndian() default false;

        Class<?> defaultValueType() default byte[].class;

        String defaultCharset() default "UTF-8";
    }

    @interface ValueDescriptor {
        // region valueLengthField
        int valueLengthFieldSize() default -1;

        boolean valueLengthFieldSizeIsLittleEndian() default false;
        // endregion valueLengthField

        // region value
        byte whenKeyIsI8() default -1;

        short whenKeyIsU8() default -1;

        short whenKeyIsI16() default -1;

        int whenKeyIsU16() default -1;

        int whenKeyIsI32() default -1;

        long whenKeyIsU32() default -1;

        String whenKeyIsStr() default "";

        Class<?> valueType() default byte[].class;

        int valueSize() default -1;

        boolean valueIsLittleEndian() default false;

        String valueCharset() default "UTF-8";
        // endregion value
    }

}
