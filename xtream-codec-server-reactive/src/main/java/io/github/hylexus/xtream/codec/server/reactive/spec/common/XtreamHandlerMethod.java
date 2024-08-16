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

package io.github.hylexus.xtream.codec.server.reactive.spec.common;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerResult;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * 当前类是从 `org.springframework.web.method.HandlerMethod` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.method.HandlerMethod`.
 *
 * @author hylexus
 */
public abstract class XtreamHandlerMethod {

    protected final Class<?> containerClass;
    protected Object containerInstance;
    protected final Method method;
    protected final XtreamMethodParameter[] parameters;

    public XtreamHandlerMethod(Class<?> containerClass, Method method) {
        this.containerClass = containerClass;
        this.method = method;
        this.parameters = this.initMethodParameters(method);
    }

    public Mono<XtreamHandlerResult> invoke(Object containerInstance, Object[] args) {
        try {
            final Object result = this.method.invoke(containerInstance, args);
            final XtreamMethodParameter returnType = new XtreamMethodParameter(-1, this.method);
            return Mono.just(new XtreamHandlerResult(this, result, returnType));
        } catch (Throwable e) {
            return Mono.error(e);
        }
    }

    private XtreamMethodParameter[] initMethodParameters(Method method) {
        final int paramCount = method.getGenericParameterTypes().length;
        final XtreamMethodParameter[] methodParameters = new XtreamMethodParameter[paramCount];
        for (int i = 0; i < paramCount; i++) {
            final XtreamMethodParameter parameter = new XtreamMethodParameter(i, method);
            methodParameters[i] = parameter;
        }
        return methodParameters;
    }

    public XtreamMethodParameter[] getParameters() {
        return parameters;
    }


    public Method getMethod() {
        return method;
    }

    public Class<?> getContainerClass() {
        return containerClass;
    }

    public Object getContainerInstance() {
        return containerInstance;
    }

    public void setContainerInstance(Object containerInstance) {
        this.containerInstance = containerInstance;
    }
}
