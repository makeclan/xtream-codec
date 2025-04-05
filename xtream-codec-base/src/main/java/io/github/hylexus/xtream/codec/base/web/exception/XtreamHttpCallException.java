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

package io.github.hylexus.xtream.codec.base.web.exception;

import io.github.hylexus.xtream.codec.base.utils.Numbers;
import io.github.hylexus.xtream.codec.base.web.domain.values.RespCode;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class XtreamHttpCallException extends XtreamHttpException {
    public XtreamHttpCallException(int httpStatus, String code, String error) {
        super(httpStatus, code, error);
    }

    public XtreamHttpCallException(HttpStatus httpStatus, String code, @Nullable String error) {
        super(httpStatus, code, error);
    }

    public XtreamHttpCallException(HttpStatus httpStatus, RespCode apiCode, @Nullable String error) {
        super(httpStatus, apiCode, error);
    }

    public static XtreamHttpCallException from(Map<String, Object> errorResponse) {
        final Integer httpStatus = Numbers.parseInteger(errorResponse.get("httpStatus")).orElse(500);
        final Object code = errorResponse.get("code");
        return new XtreamHttpCallException(
                httpStatus,
                code == null ? null : code.toString(),
                (String) errorResponse.get("error")
        );
    }

}
