package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import lombok.ToString;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@ToString
public class MethodPropertyGetter implements BeanPropertyMetadata.PropertyGetter {

    private final Method method;

    public MethodPropertyGetter(Method method) {
        this.method = method;
    }

    @Override
    public Object getProperty(BeanPropertyMetadata metadata, Object instance) {
        return ReflectionUtils.invokeMethod(method, instance);
    }

}