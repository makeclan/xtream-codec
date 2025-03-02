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

package io.github.hylexus.xtream.codec.core.tracker;

import java.util.StringJoiner;

public class PrependLengthFieldSpan extends BaseSpan {
    private final String fieldName;
    private final String fieldCodec;
    private Object value;
    private String hexString;
    private final String fieldDesc;

    public PrependLengthFieldSpan(BaseSpan parent, String fieldName, String fieldCodec, Object value, String hexString, String fieldDesc) {
        super(parent);
        this.fieldName = fieldName;
        this.fieldCodec = fieldCodec;
        this.value = value;
        this.hexString = hexString;
        this.fieldDesc = fieldDesc;
    }

    public PrependLengthFieldSpan setValue(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public PrependLengthFieldSpan setHexString(String hexString) {
        this.hexString = hexString;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldCodec() {
        return fieldCodec;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String getHexString() {
        return hexString;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PrependLengthFieldSpan.class.getSimpleName() + "[", "]")
                .add("fieldName='" + fieldName + "'")
                .add("fieldDesc='" + fieldDesc + "'")
                .add("fieldCodec='" + fieldCodec + "'")
                .add("value=" + value)
                .add("hexString='" + hexString + "'")
                .toString();
    }
}
