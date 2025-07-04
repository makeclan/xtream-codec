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


import io.github.hylexus.xtream.codec.base.web.domain.values.RespCode;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

import static io.github.hylexus.xtream.codec.base.utils.XtreamBaseInternalUtils.isBlankStr;

public class XtreamHttpException extends RuntimeException {
    private final int httpStatus;
    private final String code;
    private final String error;

    public XtreamHttpException(int httpStatus, String code, String error) {
        super(error);
        this.httpStatus = httpStatus;
        this.code = isBlankStr(code) ? "http:" + httpStatus : code;
        this.error = error;
    }

    public XtreamHttpException(HttpStatus httpStatus, String code, @Nullable String error) {
        this(httpStatus.value(), code, isBlankStr(error) ? httpStatus.getReasonPhrase() : error);
    }

    public XtreamHttpException(HttpStatus httpStatus, RespCode apiCode, @Nullable String error) {
        this(
                httpStatus.value(),
                apiCode.code(),
                isBlankStr(error)
                        ? (isBlankStr(apiCode.message()) ? httpStatus.getReasonPhrase() : apiCode.message())
                        : error
        );
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getError() {
        return error;
    }

    public String getCode() {
        return code;
    }

    public static XtreamHttpException notFound(@Nullable String error) {
        return new XtreamResourceNotFoundException(error);
    }

    public static XtreamHttpException notFound(String code, @Nullable String error) {
        return new XtreamResourceNotFoundException(code, error);
    }

    public static XtreamHttpException timeout(@Nullable String error) {
        return new XtreamHttpGatewayTimeoutException(error);
    }

    public static XtreamHttpException timeout(String code, @Nullable String error) {
        return new XtreamHttpGatewayTimeoutException(code, error);
    }

    public static XtreamHttpException badRequest(@Nullable String error) {
        return new XtreamBadRequestException(error);
    }

    public static XtreamHttpException badRequest(String code, @Nullable String error) {
        return new XtreamBadRequestException(code, error);
    }

}
