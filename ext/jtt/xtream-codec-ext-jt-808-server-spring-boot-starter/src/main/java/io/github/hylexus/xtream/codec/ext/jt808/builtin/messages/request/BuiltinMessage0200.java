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

import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.github.hylexus.xtream.codec.core.annotation.XtreamFieldMapDescriptor;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMessage64;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMessage65;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMessage66;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMessage67;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 位置信息汇报 0x0200
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage0200 {
    // 报警标志  DWORD(4)
    @Preset.JtStyle.Dword
    private long alarmFlag;

    // 状态  DWORD(4)
    @Preset.JtStyle.Dword
    private long status;

    // 纬度  DWORD(4)
    @Preset.JtStyle.Dword
    private long latitude;

    // 经度  DWORD(4)
    @Preset.JtStyle.Dword
    private long longitude;

    // 高程  WORD(2)
    @Preset.JtStyle.Word
    private int altitude;

    // 速度  WORD(2)
    @Preset.JtStyle.Word
    private int speed;

    // 方向  WORD(2)
    @Preset.JtStyle.Word
    private int direction;

    // 时间  BCD[6] yyMMddHHmmss
    @Preset.JtStyle.BCD(length = 6)
    private String time;

    // 长度：消息体长度减去前面的 28 字节

    @Preset.JtStyle.Map
    @XtreamFieldMapDescriptor(
            keyDescriptor = @XtreamFieldMapDescriptor.KeyDescriptor(type = XtreamFieldMapDescriptor.KeyType.u8),
            valueLengthFieldDescriptor = @XtreamFieldMapDescriptor.ValueLengthFieldDescriptor(length = 1),
            valueDecoderDescriptors = @XtreamFieldMapDescriptor.ValueDecoderDescriptors(
                    defaultValueDecoderDescriptor = @XtreamFieldMapDescriptor.ValueDecoderDescriptor(javaType = byte[].class),
                    valueDecoderDescriptors = {
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x01, config = @XtreamField(length = 4), javaType = Long.class, desc = "里程，DWORD，1/10km，对应车上里程表读数"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x02, config = @XtreamField(length = 2), javaType = Integer.class, desc = "油量，WORD，1/10L，对应车上油量表读数"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x03, config = @XtreamField(length = 2), javaType = Integer.class, desc = "行驶记录功能获取的速度，WORD，1/10km/h"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x04, config = @XtreamField(length = 2), javaType = Integer.class, desc = "需要人工确认报警事件的 ID，WORD，从 1 开始计数"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x11, javaType = byte[].class, desc = "长度1或5；超速报警附加信息见 表 28"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x25, config = @XtreamField(length = 4), javaType = Long.class, desc = "扩展车辆信号状态位，定义见 表 31"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x30, config = @XtreamField(length = 1), javaType = Short.class, desc = "数据类型为 BYTE，无线通信网络信号强度"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x31, config = @XtreamField(length = 1), javaType = Short.class, desc = "数据类型为 BYTE，GNSS定位卫星数"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x64, javaType = BuiltinMessage64.class, desc = "苏标: 高级驾驶辅助报警信息，定义见表 4-15"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x65, javaType = BuiltinMessage65.class, desc = "苏标: 驾驶员状态监测系统报警信息，定义见表 4-17"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x66, javaType = BuiltinMessage66.class, desc = "苏标: 胎压监测系统报警信息，定义见表 4-18"),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU8 = 0x67, javaType = BuiltinMessage67.class, desc = "苏标: 盲区监测系统报警信息，定义见表 4-20"),
                    }
            )
    )
    private Map<Short, Object> extraItems;
    // @Preset.JtStyle.List
    // private List<ExtraItem> extraItems;

    @Setter
    @Getter
    @ToString
    public static class ExtraItem {
        // 附加信息ID   BYTE(1~255)
        @Preset.JtStyle.Byte
        private short id;
        // 附加信息长度   BYTE(1~255)
        @Preset.JtStyle.Byte
        private short contentLength;
        // 附加信息内容  BYTE[N]
        @Preset.JtStyle.Bytes(lengthExpression = "getContentLength()")
        private byte[] content;

        public ExtraItem() {
        }

        public ExtraItem(short id, short length, byte[] content) {
            this.id = id;
            this.contentLength = length;
            this.content = content;
        }
    }
}
