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

package io.github.hylexus.xtream.codec.common.utils;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author hylexus
 */
public class XtreamTypes {
    static final Map<Class<?>, Integer> DEFAULT_SIZE_MAPPING = new HashMap<>();
    static final Set<Class<?>> BASIC_TYPES = Collections.unmodifiableSet(DEFAULT_SIZE_MAPPING.keySet());

    static {
        DEFAULT_SIZE_MAPPING.put(byte.class, 1);
        DEFAULT_SIZE_MAPPING.put(Byte.class, 1);
        DEFAULT_SIZE_MAPPING.put(char.class, 1);
        DEFAULT_SIZE_MAPPING.put(Character.class, 1);
        DEFAULT_SIZE_MAPPING.put(short.class, 2);
        DEFAULT_SIZE_MAPPING.put(Short.class, 2);
        DEFAULT_SIZE_MAPPING.put(int.class, 4);
        DEFAULT_SIZE_MAPPING.put(Integer.class, 4);
        DEFAULT_SIZE_MAPPING.put(float.class, 4);
        DEFAULT_SIZE_MAPPING.put(Float.class, 4);
        DEFAULT_SIZE_MAPPING.put(long.class, 8);
        DEFAULT_SIZE_MAPPING.put(Long.class, 8);
        DEFAULT_SIZE_MAPPING.put(double.class, 8);
        DEFAULT_SIZE_MAPPING.put(Double.class, 8);
    }

    public static boolean isNumberType(Class<?> targetType) {
        return BASIC_TYPES.contains(targetType);
    }

    public static boolean isBasicType(Class<?> targetType) {
        return isNumberType(targetType)
                || String.class == targetType
                || byte[].class == targetType
                || Byte[].class == targetType;
    }

    public static Optional<Integer> getDefaultSizeInBytes(Class<?> cls) {
        return Optional.ofNullable(DEFAULT_SIZE_MAPPING.get(cls));
    }

    public static BeanPropertyMetadata.FiledDataType detectFieldDataType(Class<?> type) {
        return BeanPropertyMetadata.findAnnotation(XtreamField.class, type)
                .map(XtreamField::dataType)
                .filter(it -> it != BeanPropertyMetadata.FiledDataType.unknown)
                .orElseGet(() -> {
                    if (XtreamTypes.isBasicType(type)) {
                        return BeanPropertyMetadata.FiledDataType.basic;
                    }
                    if (Collection.class.isAssignableFrom(type)) {
                        return BeanPropertyMetadata.FiledDataType.sequence;
                    }
                    if (Map.class.isAssignableFrom(type)) {
                        return BeanPropertyMetadata.FiledDataType.map;
                    }
                    return BeanPropertyMetadata.FiledDataType.nested;
                });
    }

    public static BeanPropertyMetadata.FiledDataType detectFieldDataType(Field field) {
        return BeanPropertyMetadata.findAnnotation(XtreamField.class, field)
                .map(XtreamField::dataType)
                .filter(it -> it != BeanPropertyMetadata.FiledDataType.unknown)
                .orElseGet(() -> {
                    if (XtreamTypes.isBasicType(field.getType())) {
                        return BeanPropertyMetadata.FiledDataType.basic;
                    }
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        return BeanPropertyMetadata.FiledDataType.sequence;
                    }
                    if (Map.class.isAssignableFrom(field.getType())) {
                        return BeanPropertyMetadata.FiledDataType.map;
                    }
                    return BeanPropertyMetadata.FiledDataType.nested;
                });
    }

}
