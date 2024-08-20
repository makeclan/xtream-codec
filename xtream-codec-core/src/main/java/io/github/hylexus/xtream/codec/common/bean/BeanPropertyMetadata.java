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

package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.ContainerInstanceFactory;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Optional;

public interface BeanPropertyMetadata {

    static <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationClass, AnnotatedElement targetClass) {
        final A mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(targetClass, annotationClass);
        return Optional.ofNullable(mergedAnnotation);
    }

    <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationClass);

    String name();

    Class<?> rawClass();

    FiledDataType dataType();

    Field field();

    default ContainerInstanceFactory containerInstanceFactory() {
        return ContainerInstanceFactory.PLACEHOLDER;
    }

    PropertyGetter propertyGetter();

    PropertySetter propertySetter();

    FieldCodec<?> fieldCodec();

    FieldLengthExtractor fieldLengthExtractor();

    FieldConditionEvaluator conditionEvaluator();

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
        map,
        unknown
    }

    interface PropertySetter {
        void setProperty(BeanPropertyMetadata metadata, Object instance, Object value);
    }

    interface PropertyGetter {
        Object getProperty(BeanPropertyMetadata metadata, Object instance);
    }

}
