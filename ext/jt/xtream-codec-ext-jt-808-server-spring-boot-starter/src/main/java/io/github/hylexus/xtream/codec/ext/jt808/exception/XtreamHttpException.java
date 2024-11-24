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

package io.github.hylexus.xtream.codec.ext.jt808.exception;

import io.github.hylexus.xtream.codec.ext.jt808.domain.RespCode;

public class XtreamHttpException extends RuntimeException {
    private final RespCode apiCode;

    public XtreamHttpException(RespCode apiCode) {
        this(apiCode.message(), apiCode);
    }

    public XtreamHttpException(String message, RespCode apiCode) {
        super(message);
        this.apiCode = apiCode;
    }

    public XtreamHttpException(String message, Throwable cause, RespCode apiCode) {
        super(message, cause);
        this.apiCode = apiCode;
    }

    public XtreamHttpException(Throwable cause, RespCode apiCode) {
        super(cause);
        this.apiCode = apiCode;
    }

    public XtreamHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RespCode apiCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.apiCode = apiCode;
    }
}
