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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;

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
        // todo 优化
        return Optional.ofNullable(AnnotatedElementUtils.getMergedAnnotation(this.method.getParameters()[this.index], annotationType));
        // final Annotation[] annotations = this.getParameterAnnotations();
        // for (Annotation ann : annotations) {
        //     if (annotationType.isInstance(ann)) {
        //         return Optional.of((A) ann);
        //     }
        // }
        // return Optional.empty();
    }

    public boolean hasMethodAnnotation(Class<? extends Annotation> annotationType) {
        // return this.method.isAnnotationPresent(annotationType);
        // todo 优化
        return AnnotatedElementUtils.hasAnnotation(this.method, annotationType);
    }

    public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
        // return this.method.isAnnotationPresent(annotationType);
        // todo 优化
        return AnnotatedElementUtils.getMergedAnnotation(this.method, annotationType);
    }

    public boolean hasReturnTypeAnnotation(Class<? extends Annotation> annotationType) {
        // todo 优化
        return AnnotatedElementUtils.hasAnnotation(this.method.getAnnotatedReturnType(), annotationType);
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
