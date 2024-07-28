/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.common.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 当前类是从 `org.springframework.core.MethodParameter` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.core.MethodParameter`.
 *
 * @author hylexus
 */
public class XtreamMethodParameter {
    private static final Logger log = LoggerFactory.getLogger(XtreamMethodParameter.class);

    private final Method method;
    private final int index;
    private final List<Type> genericType = new ArrayList<>();
    private final Class<?> containerClass;

    private Class<?> parameterType;
    private volatile Annotation[] parameterAnnotations;

    public XtreamMethodParameter(int index, Method method) {
        this.index = index;
        this.method = method;
        this.containerClass = this.method.getDeclaringClass();
        this.init();
    }

    private void init() {
        if (this.index < -1 || this.index >= this.method.getParameterCount()) {
            throw new IllegalArgumentException("Invalid index : " + this.index);
        }

        final Type type;
        if (this.index == -1) {
            this.parameterType = this.method.getReturnType();
            // 方法返回值
            type = this.method.getGenericReturnType();
        } else {
            // 方法参数
            type = this.method.getGenericParameterTypes()[this.index];
        }

        this.initGenericType(type);
    }

    private void initGenericType(Type type) {
        if (type instanceof Class<?> cls) {
            this.parameterType = cls;
        } else if (type instanceof ParameterizedType parameterizedType) {
            this.parameterType = (Class<?>) parameterizedType.getRawType();
            this.genericType.addAll(Arrays.asList(parameterizedType.getActualTypeArguments()));
        } else {
            log.error("Unsupported type : {}", type);
        }
    }

    public Annotation[] getParameterAnnotations() {
        if (this.parameterAnnotations == null) {
            synchronized (this) {
                if (this.parameterAnnotations == null) {
                    if (this.index >= 0) {
                        final Annotation[][] annotations = this.method.getParameterAnnotations();
                        this.parameterAnnotations = annotations[this.index];
                    } else {
                        this.parameterAnnotations = new Annotation[0];
                    }
                }
            }
        }
        return this.parameterAnnotations;
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<A> getParameterAnnotation(Class<A> annotationType) {
        final Annotation[] annotations = this.getParameterAnnotations();
        for (Annotation ann : annotations) {
            if (annotationType.isInstance(ann)) {
                return Optional.of((A) ann);
            }
        }
        return Optional.empty();
    }

    public boolean hasMethodAnnotation(Class<? extends Annotation> annotationType) {
        return this.method.isAnnotationPresent(annotationType);
    }

    public List<Type> getGenericType() {
        return genericType;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Class<?> getContainerClass() {
        return containerClass;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final XtreamMethodParameter that = (XtreamMethodParameter) o;
        return index == that.index
                && containerClass == that.containerClass
                && method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return 31 * this.method.hashCode() + this.index;
    }

    @Override
    public String toString() {
        return "XtreamMethodParameter{"
                + "index=" + index
                + ", containerClass=" + containerClass
                + ", parameterType=" + parameterType
                + ", genericType=" + genericType
                + ", parameterAnnotations=" + Arrays.toString(parameterAnnotations)
                + '}';
    }

}
