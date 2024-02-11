package io.github.hylexus.xtream.codec.core.type;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

public @interface Preset {
    @interface RustStyle {
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

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "utf-8")
        @interface string {

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

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.sequence)
        @interface list {

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
            int order();

            @AliasFor(annotation = XtreamField.class, attribute = "littleEndian")
            boolean littleEndian() default false;

            @AliasFor(annotation = XtreamField.class, attribute = "condition")
            String condition() default "";
        }

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "bcd_8421")
        @interface BCD {
            @AliasFor(annotation = XtreamField.class, attribute = "order")
            int order();

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

        @Target({ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "GBK")
        @interface STRING {
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
        @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.sequence)
        @interface List {
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
}
