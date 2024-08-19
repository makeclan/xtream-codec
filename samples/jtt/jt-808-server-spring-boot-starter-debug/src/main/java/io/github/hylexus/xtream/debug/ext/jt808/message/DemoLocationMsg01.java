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

package io.github.hylexus.xtream.debug.ext.jt808.message;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class DemoLocationMsg01 {

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

    // 速度  WORD(2)
    @Preset.RustStyle.u16
    private int speed;

    // 方向  WORD(2)
    @Preset.RustStyle.u16
    private int direction;

    // 时间  BCD[6] yyMMddHHmmss
    @Preset.RustStyle.str(charset = "bcd_8421", length = 6)
    private String time;

    // 长度：消息体长度减去前面的 28 字节
    @Preset.RustStyle.list
    private List<ExtraItem> extraItems;
    // endregion 消息体

    @Setter
    @Getter
    @ToString
    public static class ExtraItem {
        // 附加信息ID   BYTE(1~255)
        @Preset.RustStyle.u8
        private short id;
        // 附加信息长度   BYTE(1~255)
        @Preset.RustStyle.u8
        private short contentLength;
        // 附加信息内容  BYTE[N]
        @Preset.RustStyle.byte_array(lengthExpression = "getContentLength()")
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
