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

package io.github.hylexus.xtream.debug.codec.core.demo02;

import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity02ForJunitPurpose;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class RustStyleDebugEntity02ForEncode implements DebugEntity02ForJunitPurpose {

    // region 消息头
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
    // endregion 消息头

    // region 消息体
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

    // 高程  WORD(2)
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
    // endregion 消息体

    // 校验码
    @Preset.RustStyle.i8
    private byte checkSum;


    /**
     * 实现的这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    @Setter
    @Getter
    @ToString
    public static class ExtraItem implements ExtraItemForJunitPurpose {
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
