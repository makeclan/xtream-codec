package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import lombok.ToString;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@ToString
public class FiledPropertyGetter implements BeanPropertyMetadata.PropertyGetter {
    private final Field field;

    public FiledPropertyGetter(Field field) {
        this.field = field;
    }

    @Override
    public Object getProperty(BeanPropertyMetadata metadata, Object instance) {
        return ReflectionUtils.getField(field, instance);
    }
}