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

package io.github.hylexus.xtream.codec.base.web.domain.values;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author hylexus
 */
public class Resp<T> {
    private int status;

    private String code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @SuppressWarnings("unused")
    public Resp() {
    }

    public Resp(int status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <E> Resp<E> of(RespCode code, E data) {
        return new Resp<>(code.status(), code.code(), code.message(), data);
    }

    public static <E> Resp<E> of(int status, String code, String message, E data) {
        return new Resp<>(status, code, message, data);
    }

    public static <E> Resp<E> failure(RespCode code) {
        return failure(code, null);
    }

    public static <E> Resp<E> failure(RespCode code, String msg) {
        return new Resp<>(code.status(), code.code(), msg == null ? code.message() : msg, null);
    }

    public static <E> Resp<E> failure(int status, String code, String msg) {
        return new Resp<>(status, code, msg, null);
    }

    public static <E> Resp<E> badRequest(String msg) {
        final DefaultRespCode badRequest = DefaultRespCode.BAD_REQUEST;
        return new Resp<>(badRequest.status(), badRequest.code(), msg == null ? badRequest.message() : msg, null);
    }

    // getters and setters
    public int getStatus() {
        return status;
    }

    public Resp<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Resp<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Resp<T> setMessage(String message) {
        this.message = message;
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
