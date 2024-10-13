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

package io.github.hylexus.xtream.codec.core.type.wrapper;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringWrapperUtf8 implements DataWrapper<String> {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "UTF-8")
    protected String value;
    protected int length;

    public StringWrapperUtf8() {
    }

    public StringWrapperUtf8(String value) {
        this.value = value;
        this.length = value.getBytes(UTF_8).length;
    }

    @Override
    public void writeTo(ByteBuf output) {
        output.writeCharSequence(this.value, UTF_8);
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public void value(String value) {
        this.value = value;
        this.length = value.getBytes(UTF_8).length;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public void length(int length) {
        this.length = length;
    }
}
