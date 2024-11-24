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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.codec;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ParameterItem {

    // 自定义 FieldCodec 时就不用加 @Preset.JtStyle.Dword 注解了
    private long parameterId;

    // 自定义 FieldCodec 时就不用加 @Preset.JtStyle.Byte 注解了
    private short parameterLength;

    // 自定义 FieldCodec 时就不用加 @Preset.JtStyle.RuntimeType 注解了
    private Object parameterValue;

    public ParameterItem() {
    }

    public ParameterItem(long parameterId, short parameterLength, Object parameterValue) {
        this.parameterId = parameterId;
        this.parameterLength = parameterLength;
        this.parameterValue = parameterValue;
    }
}
