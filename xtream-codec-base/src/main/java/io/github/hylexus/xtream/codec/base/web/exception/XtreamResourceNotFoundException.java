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


import io.github.hylexus.xtream.codec.base.web.domain.values.DefaultRespCode;
import io.github.hylexus.xtream.codec.base.web.domain.values.RespCode;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public class XtreamResourceNotFoundException extends XtreamHttpException {

    public XtreamResourceNotFoundException(String code, @Nullable String error) {
        super(HttpStatus.NOT_FOUND.value(), code, error);
    }

    public XtreamResourceNotFoundException(@Nullable String message) {
        this(DefaultRespCode.NOT_FOUND.code(), message);
    }

    public XtreamResourceNotFoundException(RespCode code, @Nullable String error) {
        this(code.code(), error);
    }

}
