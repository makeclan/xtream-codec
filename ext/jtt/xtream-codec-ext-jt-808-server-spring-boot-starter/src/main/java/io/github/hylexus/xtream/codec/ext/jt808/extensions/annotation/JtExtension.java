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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.annotation;

import io.github.hylexus.xtream.codec.core.annotation.XtreamDateTimeField;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
public @interface JtExtension {

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @XtreamDateTimeField(pattern = JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT, charset = "bcd_8421")
    @interface BcdDateTime {

        @AliasFor(annotation = XtreamDateTimeField.class, attribute = "pattern")
        String pattern() default JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT;

        @AliasFor(annotation = XtreamDateTimeField.class, attribute = "length")
        int length() default 6;

        @AliasFor(annotation = XtreamDateTimeField.class, attribute = "order")
        int order() default -1;

        @AliasFor(annotation = XtreamDateTimeField.class, attribute = "condition")
        String condition() default "";

    }
}
