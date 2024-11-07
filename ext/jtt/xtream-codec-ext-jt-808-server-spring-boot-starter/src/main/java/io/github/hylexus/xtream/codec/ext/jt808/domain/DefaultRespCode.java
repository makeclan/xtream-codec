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

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author hylexus
 */
public enum DefaultRespCode implements RespCode {
    OK(0, HttpResponseStatus.OK.reasonPhrase()),
    SERVER_ERROR(500, HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase()),
    BAD_REQUEST(400, HttpResponseStatus.BAD_REQUEST.reasonPhrase()),
    NOT_FOUND(404, HttpResponseStatus.NOT_FOUND.reasonPhrase()),
    ;

    private final int code;
    private final String msg;

    DefaultRespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return msg;
    }
}
