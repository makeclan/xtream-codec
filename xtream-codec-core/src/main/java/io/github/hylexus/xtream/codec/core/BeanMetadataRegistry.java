package io.github.hylexus.xtream.codec.core;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;

import java.beans.PropertyDescriptor;
import java.util.function.Function;

public interface BeanMetadataRegistry {

    BeanMetadata getBeanMetadata(Class<?> beanClass);

    BeanMetadata getBeanMetadata(Class<?> beanClass, Function<PropertyDescriptor, BeanPropertyMetadata> creator);
}
