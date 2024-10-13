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

public class U32Wrapper implements DataWrapper<Long> {

    @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.basic, length = 4)
    private Long value;

    public U32Wrapper() {
    }

    public U32Wrapper(Long value) {
        this.value = value;
    }

    @Override
    public void writeTo(ByteBuf output) {
        output.writeInt(value.intValue());
    }

    @Override
    public Long value() {
        return this.value;
    }

    @Override
    public void value(Long value) {
        this.value = value;
    }

    @Override
    public int length() {
        return 4;
    }

}
