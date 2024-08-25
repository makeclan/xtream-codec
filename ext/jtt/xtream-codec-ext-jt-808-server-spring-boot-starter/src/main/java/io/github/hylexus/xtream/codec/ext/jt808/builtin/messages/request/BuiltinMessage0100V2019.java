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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 终端注册(2019)
 *
 * @author hylexus
 * @see io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8100
 */
@Getter
@Setter
@ToString
public class BuiltinMessage0100V2019 {
    // 1. [0-2) WORD 省域ID
    @Preset.JtStyle.Word
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @Preset.JtStyle.Word
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @Preset.JtStyle.Bytes(length = 11)
    private String manufacturerId;

    // 4. [9-17) BYTE[8] 终端型号
    @Preset.JtStyle.Bytes(length = 30)
    private String terminalType;

    // 5. [17-24) BYTE[7] 终端ID
    @Preset.JtStyle.Bytes(length = 30)
    private String terminalId;

    // 6. [24]   BYTE    车牌颜色
    @Preset.JtStyle.Byte
    private short color;

    // 7. [25,n)   String    车辆标识
    @Preset.JtStyle.Str
    private String carIdentifier;
}
