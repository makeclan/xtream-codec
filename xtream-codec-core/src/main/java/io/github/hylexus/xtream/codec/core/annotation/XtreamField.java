package io.github.hylexus.xtream.codec.core.annotation;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.utils.XtreamTypes;
import io.github.hylexus.xtream.codec.core.FieldCodec;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
// @Repeatable(XtreamFieldContainer.class)
public @interface XtreamField {

    BeanPropertyMetadata.FiledDataType dataType() default BeanPropertyMetadata.FiledDataType.unknown;

    int order() default -1;

    int length() default -1;

    String lengthExpression() default "";

    /**
     * 只有 {@link String} 类型用到
     */
    String charset() default "GBK";

    /**
     * @see XtreamTypes#isNumberType(Class)
     */
    boolean littleEndian() default false;

    String condition() default "";

    Class<? extends FieldCodec<?>> fieldCodec() default FieldCodec.Placeholder.class;
}