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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 苏标-表-4-20 盲区监测系统报警定义数据格式
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class BuiltinMessage67 {

    // offset[0,4) DWORD 报警ID 按照报警先后，从0开始循环累加，不区分报警类型。
    @Preset.JtStyle.Dword
    private long alarmId;

    // offset[4,5) BYTE
    // 0x00：不可用
    // 0x01：开始标志
    // 0x02：结束标志
    // 该字段仅适用于有开始和结束标志类型的报警或事件，报警类型或事件类型无开始和结束标志，则该位不可用，填入0x00即可。
    @Preset.JtStyle.Byte
    private short status;

    // offset[5,6) BYTE 报警/事件类型
    // 0x01：后方接近报警
    // 0x02：左侧后方接近报警
    // 0x03：右侧后方接近报警
    @Preset.JtStyle.Byte
    private short alarmType;

    // offset[12,13) BYTE 车速
    @Preset.JtStyle.Byte
    private short speed;

    // offset[13,15) WORD 高程
    @Preset.JtStyle.Word
    private int height;

    // offset[15,19) DWORD 纬度
    @Preset.JtStyle.Dword
    private long latitude;

    // offset[19,23) DWORD 经度
    @Preset.JtStyle.Dword
    private long longitude;

    // offset[23,29) BCD[6] 日期时间
    @Preset.JtStyle.BCD(length = 6)
    private LocalDateTime datetime;

    // offset[29,31] WORD 车辆状态
    @Preset.JtStyle.Word
    private int vehicleStatus;
    // offset[31,31+16) BYTE[16] 报警标识号
    // 报警识别号定义见表4-16
    @Preset.JtStyle.Object
    private AlarmIdentifier alarmIdentifier;

}
