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

package io.github.hylexus.xtream.debug.codec.core.demo04;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.debug.codec.core.demo04.spec.BaseJt808Msg;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DemoJt808Msg0100V2019 extends BaseJt808Msg {
    // 1. [0-2) WORD 省域ID
    @Preset.RustStyle.u16
    private int provinceId;

    // 2. [2-4) WORD 市县域ID
    @Preset.RustStyle.u16
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @Preset.RustStyle.str(length = 11, charset = "gbk")
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @Preset.RustStyle.str(length = 30, charset = "gbk")
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端ID
    @Preset.RustStyle.str(length = 30, charset = "gbk")
    private String terminalId;

    // 6. [75]   BYTE    车牌颜色
    @Preset.RustStyle.u8
    private byte color;

    /**
     * 7. [76,n)   String    车辆标识
     * <p>
     * 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
     * <p>
     * 提示: 最后一个 String 类型的字段可以不指定长度: 意味着长度是后续所有的字节数
     *
     * @see io.github.hylexus.xtream.codec.core.impl.codec.StringFieldCodec#deserialize(FieldCodec.DeserializeContext, ByteBuf, int)
     */
    // 1. 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
    // @Preset.RustStyle.str(charset = "gbk", lengthExpression = "msgBodyLength() - (1 + 30 + 30 +11 +2 +2)")
    // 2. 不指定长度(意味着长度是后续所有的字节数)
    @Preset.RustStyle.str(charset = "gbk")
    private String carIdentifier;

}
