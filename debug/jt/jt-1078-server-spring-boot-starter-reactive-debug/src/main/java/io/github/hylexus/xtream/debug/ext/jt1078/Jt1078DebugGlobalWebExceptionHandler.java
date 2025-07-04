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

package io.github.hylexus.xtream.debug.ext.jt1078;

import io.github.hylexus.xtream.codec.base.web.domain.values.DefaultRespCode;
import io.github.hylexus.xtream.codec.base.web.domain.values.Resp;
import io.github.hylexus.xtream.codec.base.web.domain.vo.XtreamWebErrorResponse;
import io.github.hylexus.xtream.codec.base.web.exception.XtreamHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RestControllerAdvice
public class Jt1078DebugGlobalWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(Jt1078DebugGlobalWebExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public XtreamWebErrorResponse handleThrowable(Throwable ex) {
        log.error(ex.getMessage(), ex);
        final Resp<Object> body = Resp.failure(DefaultRespCode.SERVER_ERROR);
        return new XtreamWebErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, body);
    }

    @ExceptionHandler(XtreamHttpException.class)
    public XtreamWebErrorResponse processXtreamHttpException(XtreamHttpException e) {
        final Resp<Object> body = Resp.failure(e.getHttpStatus(), e.getCode(), e.getError());
        final HttpStatus httpStatus = HttpStatus.valueOf(e.getHttpStatus());
        return new XtreamWebErrorResponse(httpStatus, body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public XtreamWebErrorResponse processResponseStatusException(ResponseStatusException e) {
        final HttpStatusCode httpStatusCode = e.getStatusCode();
        final Resp<Object> body = Resp.failure(httpStatusCode.value(), String.valueOf(httpStatusCode.value()), e.getReason());
        return new XtreamWebErrorResponse(httpStatusCode, body);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public XtreamWebErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        final String message = Optional.of(ex.getValueResults())
                .filter(r -> !r.isEmpty())
                .map(List::getFirst)
                .map(ParameterValidationResult::getResolvableErrors)
                .filter(it -> !it.isEmpty())
                .map(List::getFirst)
                .map(MessageSourceResolvable::getDefaultMessage)
                .orElse("参数异常");
        return new XtreamWebErrorResponse(HttpStatus.BAD_REQUEST, Resp.badRequest(message));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public XtreamWebErrorResponse handleWebExchangeBindException(WebExchangeBindException ex) {
        final String message = Optional.of(ex.getAllErrors()).filter(it -> !it.isEmpty())
                .map(List::getFirst)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(ex.getMessage());
        return new XtreamWebErrorResponse(HttpStatus.BAD_REQUEST, Resp.badRequest(message));
    }

}
