package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

public interface BeanPropertyMetadata {

    static <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationClass, Class<?> targetClass) {
        final A mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(targetClass, annotationClass);
        return Optional.ofNullable(mergedAnnotation);
    }

    String name();

    Class<?> rawClass();

    FiledDataType dataType();

    Field field();

    PropertyGetter propertyGetter();

    PropertySetter propertySetter();

    FieldCodec<?> fieldCodec();

    FieldLengthExtractor fieldLengthExtractor();

    FieldConditionEvaluator conditionEvaluator();

    <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationClass);

    int order();

    Object decodePropertyValue(FieldCodec.DeserializeContext context, ByteBuf input);

    void encodePropertyValue(FieldCodec.SerializeContext context, ByteBuf output, Object value);

    default void setProperty(Object instance, Object value) {
        this.propertySetter().setProperty(this, instance, value);
    }

    default Object getProperty(Object instance) {
        return this.propertyGetter().getProperty(this, instance);
    }

    enum FiledDataType {
        basic,
        nested,
        sequence,
        unknown
    }

    interface PropertySetter {
        void setProperty(BeanPropertyMetadata metadata, Object instance, Object value);
    }

    interface PropertyGetter {
        Object getProperty(BeanPropertyMetadata metadata, Object instance);
    }

}
