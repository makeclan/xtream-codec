package io.github.hylexus.xtream.codec.common.bean.impl;


import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import lombok.ToString;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@ToString
public class FieldPropertySetter implements BeanPropertyMetadata.PropertySetter {

    private final Field field;

    public FieldPropertySetter(Field field) {
        this.field = field;
    }

    @Override
    public void setProperty(BeanPropertyMetadata metadata, Object instance, Object value) {
        ReflectionUtils.setField(field, instance, value);
    }
}