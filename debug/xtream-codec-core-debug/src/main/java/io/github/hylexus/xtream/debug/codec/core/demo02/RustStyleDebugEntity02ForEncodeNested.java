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

package io.github.hylexus.xtream.debug.codec.core.demo02;

import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity02NestedForJunitPurpose;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class RustStyleDebugEntity02ForEncodeNested {

    // 消息头
    @Preset.RustStyle.struct
    private Header header;

    // 消息体
    @Preset.RustStyle.struct
    private Body body;

    // 校验码
    @Preset.RustStyle.i8
    private byte checkSum;

    /**
     * 实现的这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    @Getter
    @Setter
    @ToString
    public static class Header implements DebugEntity02NestedForJunitPurpose.DebugEntity02HeaderForJunitPurpose {
        // byte[0-2)    消息ID word(16)
        @Preset.RustStyle.u16
        private int msgId;

        // byte[2-4)    消息体属性 word(16)
        @Preset.RustStyle.u16
        private int msgBodyProps;

        // byte[4]     协议版本号
        @Preset.RustStyle.u8
        private byte protocolVersion;

        // byte[5-15)    终端手机号或设备ID bcd[10]
        @Preset.RustStyle.str(charset = "bcd_8421")
        private String terminalId;

        // byte[15-17)    消息流水号 word(16)
        @Preset.RustStyle.u16
        private int msgSerialNo;

        // byte[17-21)    消息包封装项
        @Preset.RustStyle.u32
        private Long subPackageInfo;
    }

    /**
     * 实现的这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    @Getter
    @Setter
    @ToString
    public static class Body implements DebugEntity02NestedForJunitPurpose.DebugEntity02BodyForJunitPurpose {
        // 报警标志  DWORD(4)
        @Preset.RustStyle.u32
        private long alarmFlag;

        // 状态  DWORD(4)
        @Preset.RustStyle.u32
        private long status;

        // 纬度  DWORD(4)
        @Preset.RustStyle.u32
        private long latitude;

        // 经度  DWORD(4)
        @Preset.RustStyle.u32
        private long longitude;

        // 高程  WORD(2)
        @Preset.RustStyle.u16
        private int altitude;

        // 速度  WORD(2)
        @Preset.RustStyle.u16
        private int speed;

        // 方向  WORD(2)
        @Preset.RustStyle.u16
        private int direction;

        // 时间  BCD[6] yyMMddHHmmss
        @Preset.RustStyle.str(charset = "bcd_8421")
        private String time;

        @Preset.RustStyle.list
        private List<ExtraItem> extraItems;
    }


    /**
     * 实现的这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    @Setter
    @Getter
    @ToString
    public static class ExtraItem implements DebugEntity02NestedForJunitPurpose.DebugEntity02ExtraItemForJunitPurpose {
        // 附加信息ID   BYTE(1~255)
        @Preset.RustStyle.u8
        private short id;
        // 附加信息长度   BYTE(1~255)
        @Preset.RustStyle.u8
        private short contentLength;
        // 附加信息内容  BYTE[N]
        @Preset.RustStyle.byte_array(lengthExpression = "getLength()")
        private byte[] content;

        public ExtraItem() {
        }

        public ExtraItem(short id, short contentLength, byte[] content) {
            this.id = id;
            this.contentLength = contentLength;
            this.content = content;
        }
    }
}
