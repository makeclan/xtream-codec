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