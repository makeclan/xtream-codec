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

import io.github.hylexus.xtream.codec.common.utils.XtreamConstants;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.netty.buffer.ByteBuf;

public class StringWrapperGbk implements DataWrapper<String> {

    @Preset.RustStyle.str(charset = "gbk")
    protected String value;
    protected int length;

    public StringWrapperGbk() {
    }

    public StringWrapperGbk(String value) {
        this.value = value;
        this.length = value.getBytes(XtreamConstants.CHARSET_GBK).length;
    }

    @Override
    public void writeTo(ByteBuf output) {
        output.writeCharSequence(value, XtreamConstants.CHARSET_GBK);
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public byte[] asBytes() {
        return value.getBytes(XtreamConstants.CHARSET_GBK);
    }

    @Override
    public byte asI8() {
        throw new UnsupportedOperationException();
    }

    @Override
    public short asI16() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int asI32() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String asString() {
        return value;
    }

    public StringWrapperGbk setValue(String value) {
        this.value = value;
        this.length = value.getBytes(XtreamConstants.CHARSET_GBK).length;
        return this;
    }
}
