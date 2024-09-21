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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 苏标-表-4-18 胎压监测系统报警信息数据格式
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class BuiltinMessage66 {

    // offset[0,4) DWORD 报警 ID: 按照报警先后，从0开始循环累加，不区分报警类型。
    @Preset.JtStyle.Dword
    private long alarmId;

    // offset[4,5) BYTE 标志状态
    // 0x00：不可用
    // 0x01：开始标志
    // 0x02：结束标志
    // 该字段仅适用于有开始和结束标志类型的报警或事件，报警类型或事件类型无开始和结束标志，则该位不可用，填入0x00即可
    @Preset.JtStyle.Byte
    private short status;

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

    // offset[39, 40) BYTE 报警/事件列表总数
    @Preset.JtStyle.Byte
    private short eventItemCount;

    // 报警/事件信息列表
    @Preset.JtStyle.List(condition = "getEventItemCount() > 0")
    private List<EventItem> eventItemList;

    @Data
    public static class EventItem {
        // 胎压报警位置 BYTE 报警轮胎位置编号（从左前轮开始以Z字形从00依次编号，编号与是否安装TPMS无关）
        @Preset.JtStyle.Byte
        private short offset0;

        // 报警/事件类型 WORD 0表示无报警，1表示有报警
        // bit0：胎压（定时上报）
        // bit1：胎压过高报警
        // bit2：胎压过低报警
        // bit3：胎温过高报警
        // bit4：传感器异常报警
        // bit5：胎压不平衡报警
        // bit6：慢漏气报警
        // bit7：电池电量低报警
        // bit8~bit15：自定义
        @Preset.JtStyle.Word
        private int offset2;

        // 胎压 WORD 单位 Kpa
        @Preset.JtStyle.Word
        private int offset4;

        // 胎温 WORD 单位 ℃
        @Preset.JtStyle.Word
        private int offset6;

        // 电池电量 WORD 单位 %
        @Preset.JtStyle.Word
        private int offset8;
    }
}
