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

public class StringWrapperGbk implements DataWrapper<String> {

    public static final Charset GBK = Charset.forName("GBK");

    @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, charset = "GBK")
    protected String value;
    protected int length;

    public StringWrapperGbk() {
    }

    public StringWrapperGbk(String value) {
        this.value = value;
        this.length = value.getBytes(GBK).length;
    }

    @Override
    public void writeTo(ByteBuf output) {
        output.writeCharSequence(value, GBK);
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public void value(String value) {
        this.value = value;
        this.length = value.getBytes(GBK).length;
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
