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

}
