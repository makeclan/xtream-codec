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

package io.github.hylexus.xtream.codec.ext.jt808.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author hylexus
 */
public class Resp<T> {

    private int code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Resp() {
    }

    public Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <E> Resp<E> ok(E data) {
        return of(DefaultRespCode.OK, data);
    }

    public static <E> Resp<E> of(RespCode code, E data) {
        return new Resp<>(code.code(), code.message(), data);
    }

    public static <E> Resp<E> of(int code, String message, E data) {
        return new Resp<>(code, message, data);
    }

    public static <E> Resp<E> failure(RespCode code) {
        return failure(code, null);
    }

    public static <E> Resp<E> failure(RespCode code, String msg) {
        return new Resp<>(code.code(), msg == null ? code.message() : msg, null);
    }

    public static <E> Resp<E> failure(int code, String msg) {
        return new Resp<>(code, msg, null);
    }

    public static <E> Resp<E> badRequest(String msg) {
        return new Resp<>(DefaultRespCode.BAD_REQUEST.code(), msg == null ? DefaultRespCode.BAD_REQUEST.message() : msg, null);
    }

    public static <E> Resp<E> notFound() {
        return notFound(null);
    }

    public static <E> Resp<E> notFound(String msg) {
        return new Resp<>(DefaultRespCode.NOT_FOUND.code(), msg == null ? DefaultRespCode.NOT_FOUND.message() : msg, null);
    }

    public int getCode() {
        return code;
    }

    public Resp<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Resp<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Resp<T> setData(T data) {
        this.data = data;
        return this;
    }
}
