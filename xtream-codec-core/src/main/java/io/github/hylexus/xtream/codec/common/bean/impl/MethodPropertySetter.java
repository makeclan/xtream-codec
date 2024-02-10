package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import lombok.ToString;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@ToString
public class MethodPropertySetter implements BeanPropertyMetadata.PropertySetter {
    private final Method method;

    public MethodPropertySetter(Method method) {
        this.method = method;
    }

    @Override
    public void setProperty(BeanPropertyMetadata metadata, Object instance, Object value) {
        ReflectionUtils.invokeMethod(method, instance, value);
    }
}