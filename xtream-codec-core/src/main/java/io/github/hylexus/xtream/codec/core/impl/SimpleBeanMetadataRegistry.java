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

package io.github.hylexus.xtream.codec.core.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.FieldLengthExtractor;
import io.github.hylexus.xtream.codec.common.bean.impl.BasicBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.MapBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.NestedBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.SequenceBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.BeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.FieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.XtreamCacheableClassPredicate;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.github.hylexus.xtream.codec.core.impl.codec.RuntimeTypeFieldCodec;
import io.github.hylexus.xtream.codec.core.utils.BeanUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.function.Function;

// todo 重构
public class SimpleBeanMetadataRegistry implements BeanMetadataRegistry {
    protected final Map<Class<?>, BeanMetadata> cache = new HashMap<>();
    protected final FieldCodecRegistry fieldCodecRegistry;
    protected final XtreamCacheableClassPredicate cacheableClassPredicate;

    public SimpleBeanMetadataRegistry(FieldCodecRegistry fieldCodecRegistry, XtreamCacheableClassPredicate cacheableClassPredicate) {
        this.fieldCodecRegistry = fieldCodecRegistry;
        this.cacheableClassPredicate = cacheableClassPredicate;
    }

    @Override
    public FieldCodecRegistry getFieldCodecRegistry() {
        return fieldCodecRegistry;
    }

    @Override
    public BeanMetadata getBeanMetadata(Class<?> beanClass) {
        return this.getBeanMetadata(beanClass, this::createBeanPropertyMetadata);
    }

    @Override
    public BeanMetadata getBeanMetadata(Class<?> beanClass, Function<PropertyDescriptor, BeanPropertyMetadata> creator) {
        if (!this.cacheableClassPredicate.test(beanClass)) {
            return this.doGetMetadata(beanClass, creator);
        }

        BeanMetadata result = cache.get(beanClass);
        if (result != null) {
            return result;
        }

        synchronized (SimpleBeanMetadataRegistry.class) {
            if ((result = cache.get(beanClass)) != null) {
                return result;
            }
            result = this.doGetMetadata(beanClass, creator);
            cache.put(beanClass, result);
            return result;
        }

    }

    public BasicBeanPropertyMetadata createBeanPropertyMetadata(PropertyDescriptor pd) {
        final BeanUtils.BasicPropertyDescriptor basicPropertyDescriptor = (BeanUtils.BasicPropertyDescriptor) pd;
        final BasicBeanPropertyMetadata metadata = new BasicBeanPropertyMetadata(
                pd.getName(),
                pd.getPropertyType(),
                basicPropertyDescriptor.getField(),
                // pd,
                BeanUtils.createGetter(basicPropertyDescriptor),
                BeanUtils.createSetter(basicPropertyDescriptor)
        );
        metadata.setFieldCodec(detectFieldCodec(metadata));
        return metadata;
    }

    public BeanMetadata doGetMetadata(Class<?> beanClass, Function<PropertyDescriptor, BeanPropertyMetadata> creator) {
        final BeanInfo beanInfo = BeanUtils.getBeanInfo(beanClass, this.cacheableClassPredicate, field -> AnnotatedElementUtils.findMergedAnnotation(field, XtreamField.class) != null);
        final BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        final ArrayList<BeanPropertyMetadata> pdList = new ArrayList<>();
        for (final PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            final BeanPropertyMetadata basicPropertyMetadata = creator.apply(pd);
            if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.basic) {
                pdList.add(basicPropertyMetadata);
            } else if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.struct) {
                final BeanMetadata nestedMetadata = doGetMetadata(pd.getPropertyType(), creator);
                final NestedBeanPropertyMetadata metadata = new NestedBeanPropertyMetadata(nestedMetadata, basicPropertyMetadata, null);
                pdList.add(metadata);
            } else if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.sequence) {
                final List<Class<?>> genericClass = getGenericClass(basicPropertyMetadata.field());
                final BeanMetadata valueMetadata = doGetMetadata(genericClass.getFirst(), creator);
                final NestedBeanPropertyMetadata metadata = new NestedBeanPropertyMetadata(valueMetadata, basicPropertyMetadata, new FieldLengthExtractor.ConstantFieldLengthExtractor(-2));
                final SequenceBeanPropertyMetadata seqMetadata = new SequenceBeanPropertyMetadata(basicPropertyMetadata, metadata);
                pdList.add(seqMetadata);
            } else if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.map) {
                final MapBeanPropertyMetadata mapMedata = new MapBeanPropertyMetadata(basicPropertyMetadata, fieldCodecRegistry, this);
                pdList.add(mapMedata);
            } else if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.dynamic) {
                pdList.add(basicPropertyMetadata);
            } else {
                throw new IllegalStateException("Cannot determine dataType for " + basicPropertyMetadata.field());
            }
        }
        pdList.sort(Comparator.comparing(BeanPropertyMetadata::order));
        return new BeanMetadata(beanInfo.getBeanDescriptor().getBeanClass(), BeanUtils.getConstructor(beanDescriptor), pdList);
    }

    private List<Class<?>> getGenericClass(Field field) {
        final Type genericType = field.getGenericType();
        final List<Class<?>> list = new ArrayList<>();
        if (genericType instanceof ParameterizedType) {
            for (Type actualTypeArgument : ((ParameterizedType) genericType).getActualTypeArguments()) {
                // ignore WildcardType
                // ?, ? extends Number, or ? super Integer
                if (actualTypeArgument instanceof WildcardType) {
                    continue;
                }
                list.add((Class<?>) actualTypeArgument);
            }
        }
        return list;
    }

    public FieldCodec<?> detectFieldCodec(BasicBeanPropertyMetadata metadata) {
        return this.fieldCodecRegistry.getFieldCodec(metadata).orElseGet(() -> {
            // ...
            return switch (metadata.dataType()) {
                case struct, sequence, map -> null;
                case dynamic -> RuntimeTypeFieldCodec.INSTANCE;
                default -> throw new IllegalStateException("Cannot determine FieldCodec for Field [" + metadata.field() + "]");
            };
        });
    }
}
