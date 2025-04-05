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

/**
 * @author hylexus
 */
public enum DefaultRespCode implements RespCode {
    OK(200, "000:000", "OK"),
    SERVER_ERROR(500, "000:500", "Internal Server Error"),
    BAD_REQUEST(400, "000:400", "Bad Request"),
    NOT_FOUND(404, "000:404", "Not Found"),
    REQUEST_TIMEOUT(408, "000:408", "Request Timeout"),
    GATEWAY_TIMEOUT(504, "000:504", "Gateway Timeout"),
    ;
    private final int status;
    private final String code;
    private final String msg;

    DefaultRespCode(int status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int status() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return msg;
    }
}
