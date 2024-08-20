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

package io.github.hylexus.xtream.codec.core.type;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.ContainerInstanceFactory;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
public @interface Preset {
    @interface RustStyle {

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 1)
        @interface i8 {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 1)
        @interface u8 {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 2)
        @interface i16 {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 2, littleEndian = true)
        @interface i16_le {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 2)
        @interface u16 {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 2, littleEndian = true)
        @interface u16_le {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 4)
        @interface i32 {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 4, littleEndian = true)
        @interface i32_le {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 4)
        @interface u32 {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 4, littleEndian = true)
        @interface u32_le {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "utf-8")
        @interface str {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "charset")
            String charset() default "utf-8";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.nested)
        @interface struct {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.sequence, containerInstanceFactory = ContainerInstanceFactory.ArrayListContainerInstanceFactory.class)
        @interface list {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "containerInstanceFactory")
            Class<? extends ContainerInstanceFactory> containerInstanceFactory() default ContainerInstanceFactory.ArrayListContainerInstanceFactory.class;
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.map, containerInstanceFactory = ContainerInstanceFactory.LinkedHashMapContainerInstanceFactory.class)
        @interface map {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "containerInstanceFactory")
            Class<? extends ContainerInstanceFactory> containerInstanceFactory() default ContainerInstanceFactory.LinkedHashMapContainerInstanceFactory.class;
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic)
        @interface byte_array {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }
    }

    @interface JtStyle {
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 2)
        @interface Word {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "littleEndian")
            boolean littleEndian() default false;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 4)
        @interface Dword {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "littleEndian")
            boolean littleEndian() default false;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "bcd_8421")
        @interface BCD {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 1)
        @interface Byte {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic)
        @interface Bytes {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @SuppressWarnings("checkstyle:TypeName")
        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "GBK")
        @interface Str {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "charset")
            String charset() default "GBK";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.nested)
        @interface Object {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.sequence, containerInstanceFactory = ContainerInstanceFactory.ArrayListContainerInstanceFactory.class)
        @interface List {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "containerInstanceFactory")
            Class<? extends ContainerInstanceFactory> containerInstanceFactory() default ContainerInstanceFactory.ArrayListContainerInstanceFactory.class;
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.map, containerInstanceFactory = ContainerInstanceFactory.LinkedHashMapContainerInstanceFactory.class)
        @interface Map {

            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "length")
            int length() default -1;

            @AliasFor(annotation = XtreamField.class, attribute = "lengthExpression")
            String lengthExpression() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";

            @AliasFor(annotation = XtreamField.class, attribute = "containerInstanceFactory")
            Class<? extends ContainerInstanceFactory> containerInstanceFactory() default ContainerInstanceFactory.LinkedHashMapContainerInstanceFactory.class;
        }
    }
}
