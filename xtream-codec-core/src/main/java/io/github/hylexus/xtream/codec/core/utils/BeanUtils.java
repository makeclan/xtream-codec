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

package io.github.hylexus.xtream.codec.core.utils;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.FieldPropertySetter;
import io.github.hylexus.xtream.codec.common.bean.impl.FiledPropertyGetter;
import io.github.hylexus.xtream.codec.common.bean.impl.MethodPropertyGetter;
import io.github.hylexus.xtream.codec.common.bean.impl.MethodPropertySetter;
import io.github.hylexus.xtream.codec.common.exception.BeanIntrospectionException;
import lombok.Getter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class BeanUtils {
    public static final BasicPropertyDescriptor[] EMPTY_ARRAY = {};

    private static final Map<Class<?>, BeanInfo> CACHE = new HashMap<>();

    public static BeanInfo getBeanInfo(Class<?> beanClass, Predicate<Field> fieldPredicate) throws BeanIntrospectionException {

        BeanInfo beanInfo;
        if ((beanInfo = CACHE.get(beanClass)) != null) {
            return beanInfo;
        }

        final BasicPropertyDescriptor[] propertyDescriptors = determineBasicProperties(beanClass, fieldPredicate);

        beanInfo = new SimpleBeanInfo() {
            @Override
            public BeanDescriptor getBeanDescriptor() {
                return new BeanDescriptor(beanClass);
            }

            @Override
            public BasicPropertyDescriptor[] getPropertyDescriptors() {
                return propertyDescriptors;
            }

            @Override
            public MethodDescriptor[] getMethodDescriptors() {
                throw new UnsupportedOperationException();
            }

        };

        CACHE.put(beanClass, beanInfo);
        return beanInfo;
    }

    public static BasicPropertyDescriptor[] determineBasicProperties(Class<?> beanClass, Predicate<Field> fieldPredicate) {

        final Map<String, BasicPropertyDescriptor> pdMap = new LinkedHashMap<>();

        ReflectionUtils.doWithFields(beanClass, field -> {
            if (!fieldPredicate.test(field)) {
                return;
            }
            final String name = field.getName();
            field.setAccessible(true);
            final BasicPropertyDescriptor pd = pdMap.get(name);
            if (pd == null) {
                pdMap.put(name, BasicPropertyDescriptor.of(field, null, null));
            }
        });

        ReflectionUtils.doWithMethods(beanClass, method -> {
            final String methodName = method.getName();

            boolean setter;
            int nameIndex;
            if (methodName.startsWith("set") && method.getParameterCount() == 1) {
                setter = true;
                nameIndex = 3;
            } else if (methodName.startsWith("get") && method.getParameterCount() == 0 && method.getReturnType() != Void.TYPE) {
                setter = false;
                nameIndex = 3;
            } else if (methodName.startsWith("is") && method.getParameterCount() == 0 && method.getReturnType() == boolean.class) {
                setter = false;
                nameIndex = 2;
            } else if (methodName.startsWith("with") && method.getParameterCount() == 1) {
                setter = true;
                nameIndex = 4;
            } else {
                return;
            }

            final String propertyName = StringUtils.uncapitalizeAsProperty(methodName.substring(nameIndex));
            if (propertyName.isEmpty()) {
                return;
            }

            final BasicPropertyDescriptor pd = pdMap.get(propertyName);
            if (pd != null) {
                if (setter) {
                    Method writeMethod = pd.getWriteMethod();
                    if (writeMethod == null || writeMethod.getParameterTypes()[0].isAssignableFrom(method.getParameterTypes()[0])) {
                        pd.setWriteMethod(method);
                        method.setAccessible(true);
                    }
                } else {
                    Method readMethod = pd.getReadMethod();
                    if (readMethod == null
                            || (readMethod.getReturnType() == method.getReturnType() && method.getName().startsWith("is"))) {
                        pd.setReadMethod(method);
                        method.setAccessible(true);
                    }
                }
            }
        });


        return pdMap.values().toArray(EMPTY_ARRAY);
    }

    public static BeanPropertyMetadata.PropertySetter createSetter(BeanUtils.BasicPropertyDescriptor pd) {
        if (pd.getWriteMethod() != null) {
            return new MethodPropertySetter(pd.getWriteMethod());
        }
        return new FieldPropertySetter(pd.getField());
    }

    public static BeanPropertyMetadata.PropertyGetter createGetter(BeanUtils.BasicPropertyDescriptor pd) {
        if (pd.getReadMethod() != null) {
            return new MethodPropertyGetter(pd.getReadMethod());
        }
        return new FiledPropertyGetter(pd.getField());
    }

    public static Constructor<?> getConstructor(BeanDescriptor beanDescriptor) {
        try {
            return beanDescriptor.getBeanClass().getConstructor();
        } catch (NoSuchMethodException e) {
            // throw new BeanIntrospectionException(e);
            return null;
        }
    }

    public static <T> T createNewInstance(Class<T> cls, Object... args) {
        try {
            final Constructor<T> constructor = cls.getConstructor();
            return createNewInstance(constructor, args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createNewInstance(Constructor<T> constructor, Object... args) {
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static class BasicPropertyDescriptor extends PropertyDescriptor {

        @Getter
        private final Field field;
        private Method readMethod;

        private Method writeMethod;

        public BasicPropertyDescriptor(Field field, Method readMethod, Method writeMethod)
                throws IntrospectionException {
            super(field.getName(), readMethod, writeMethod);
            this.field = field;
        }

        public static BasicPropertyDescriptor of(Field field, Method readMethod, Method writeMethod) {
            try {
                return new BasicPropertyDescriptor(field, readMethod, writeMethod);
            } catch (IntrospectionException e) {
                throw new BeanIntrospectionException(e);
            }
        }

        @Override
        public Method getReadMethod() {
            return this.readMethod;
        }

        @Override
        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
        }

        @Override
        public Method getWriteMethod() {
            return this.writeMethod;
        }

        @Override
        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        @Override
        public synchronized Class<?> getPropertyType() {
            return field.getType();
        }
    }
}
