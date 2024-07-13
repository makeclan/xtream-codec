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

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author hylexus
 */
public class XtreamTypeDescriptor {
    private Class<?> rawClass;
    private List<Type> genericType;

    private XtreamTypeDescriptor(Class<?> returnType, List<Type> genericType) {
        this.rawClass = returnType;
        this.genericType = genericType;
    }


    public static XtreamTypeDescriptor fromMethodReturnType(XtreamMethodParameter method) {
        return new XtreamTypeDescriptor(method.getContainerClass(), method.getGenericType());
    }

    public Class<?> getRawClass() {
        return rawClass;
    }

    public List<Type> getGenericType() {
        return genericType;
    }
}
