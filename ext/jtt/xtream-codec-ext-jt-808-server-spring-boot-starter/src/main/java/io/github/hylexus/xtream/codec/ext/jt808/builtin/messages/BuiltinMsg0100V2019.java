/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 内置终端注册消息体(2011)
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class BuiltinMsg0100V2019 {
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
