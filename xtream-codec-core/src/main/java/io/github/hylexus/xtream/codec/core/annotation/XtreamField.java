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

package io.github.hylexus.xtream.codec.core.annotation;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.utils.XtreamTypes;
import io.github.hylexus.xtream.codec.core.ContainerInstanceFactory;
import io.github.hylexus.xtream.codec.core.FieldCodec;

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
public @interface XtreamField {

    /**
     * 指定被当前注解标记的属性的类型: 基础类型、嵌套类型、List 类型。
     * <p>
     * 该属性主要用来确定 {@link FieldCodec}。
     */
    BeanPropertyMetadata.FiledDataType dataType() default BeanPropertyMetadata.FiledDataType.unknown;

    /**
     * 序列化/反序列化时当前属性的顺序。
     *
     * @see <a href="https://stackoverflow.com/questions/5001172/java-reflection-getting-fields-and-methods-in-declaration-order">java-reflection-getting-fields-and-methods-in-declaration-order</a>
     */
    int order() default -1;

    /**
     * 反序列化时当前属性的长度。
     *
     * @see #lengthExpression()
     */
    int length() default -1;

    /**
     * 反序列化时当前属性的长度。当长度无法直接确定时，可以指定一个表达式来确定长度。
     * <p>
     * 目前仅仅支持 <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">SpEL</a> 语法。
     *
     * @see #length()
     * @see <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">SpEL 官方文档</a>
     */
    String lengthExpression() default "";

    /**
     * 只有 {@link String} 类型用到
     */
    String charset() default "GBK";

    /**
     * 是否是小端序。
     *
     * @see XtreamTypes#isNumberType(Class)
     */
    boolean littleEndian() default false;

    /**
     * 当且仅当 {@code condition} 为 {@code true} 时，当前属性才会被序列化/反序列化。
     *
     * @see <a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">SpEL 官方文档</a>
     */
    String condition() default "";

    /**
     * 当前属性序列化/反序列化时用到的 {@link FieldCodec}。
     * <p>
     * 内置类型可以自动检测，自定义类型需要指定该属性。
     *
     * @see FieldCodec
     */
    Class<? extends FieldCodec<?>> fieldCodec() default FieldCodec.Placeholder.class;

    /**
     * 只有 {@link java.util.Map} 和 {@link java.util.List} 类型用到
     *
     * @see BeanPropertyMetadata.FiledDataType#sequence
     * @see io.github.hylexus.xtream.codec.common.bean.impl.SequenceBeanPropertyMetadata
     * @see BeanPropertyMetadata.FiledDataType#map
     * @see io.github.hylexus.xtream.codec.common.bean.impl.MapBeanPropertyMetadata
     */
    Class<? extends ContainerInstanceFactory> containerInstanceFactory() default ContainerInstanceFactory.PlaceholderContainerInstanceFactory.class;

    /**
     * 描述字段；和 jt-framework 保持一致，没有特殊作用。
     */
    String desc() default "";

}
