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
import lombok.experimental.Accessors;

/**
 * 查询终端属性应答 0x0107
 *
 * @author hylexus
 */
@Getter
@Setter
@Accessors(chain = true)
public class BuiltinMessage0107V2019 {

    /**
     * 终端类型
     * <li>bit0，0：不适用客运车辆，1：适用客运车辆</li>
     * <li>bit1，0：不适用危险品车辆，1：适用危险品车辆</li>
     * <li>bit2，0：不适用普通货运车辆，1：适用普通货运车辆</li>
     * <li>bit3，0：不适用出租车辆，1：适用出租车辆</li>
     * <li>bit6，0：不支持硬盘录像，1：支持硬盘录像</li>
     * <li>bit7，0：一体机，1：分体机</li>
     * <li>bit8，0：不适用挂车，1：适用挂车</li>
     */
    @Preset.JtStyle.Word
    private short type;

    /**
     * 制造商 ID
     */
    @Preset.JtStyle.Bytes(length = 11)
    private String manufacturerId;

    /**
     * 终端型号
     */
    @Preset.JtStyle.Bytes(length = 30)
    private String terminalType;

    /**
     * 终端 ID
     */
    @Preset.JtStyle.Bytes(length = 30)
    private String terminalId;

    /**
     * 终端 SIM 卡 ICCID
     */
    @Preset.JtStyle.Bcd(length = 10)
    private String iccid;

    /**
     * 终端硬件版本号长度
     */
    @Preset.JtStyle.Byte
    private short hardwareVersionLength;

    // lengthExpression: 只有解码才会用到
    @Preset.JtStyle.Str(lengthExpression = "getHardwareVersionLength()")
    private String hardwareVersion;

    /**
     * 终端固件版本号长度
     */
    @Preset.JtStyle.Byte
    private short firmwareVersionLength;

    /**
     * 终端固件版本号
     */
    // lengthExpression: 只有解码才会用到
    @Preset.JtStyle.Str(lengthExpression = "getFirmwareVersionLength()")
    private String firmwareVersion;

    /**
     * GNSS 模块属性
     */
    @Preset.JtStyle.Byte
    private short gnssModelProperty;

    /**
     * 通信模块属性
     */
    @Preset.JtStyle.Byte
    private short communicationModelProperty;

    // ...
    @SuppressWarnings("lombok")
    public short getHardwareVersionLength() {
        return hardwareVersionLength;
    }

    @SuppressWarnings("lombok")
    public short getFirmwareVersionLength() {
        return firmwareVersionLength;
    }
}
