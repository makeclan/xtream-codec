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

package io.github.hylexus.xtream.codec.server.reactive.spec.handler;

/**
 * 当前类是从 `org.springframework.web.reactive.HandlerResult` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `org.springframework.web.reactive.HandlerResult`.
 *
 * @author hylexus
 */
public class XtreamHandlerResult {

    private final Object handler;

    private final Object returnValue;
    private XtreamDispatchExceptionHandler exceptionHandler;

    public XtreamHandlerResult(Object handler, Object returnValue) {
        this.handler = handler;
        this.returnValue = returnValue;
    }

    public Object getHandler() {
        return this.handler;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public XtreamDispatchExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
