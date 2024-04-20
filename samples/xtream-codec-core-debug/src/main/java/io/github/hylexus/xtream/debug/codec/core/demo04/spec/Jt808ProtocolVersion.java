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

package io.github.hylexus.xtream.debug.codec.core.demo04.spec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public enum Jt808ProtocolVersion {
    VERSION_2013("2013", (byte) 0),
    VERSION_2011("2011", (byte) 0),
    VERSION_2019("2019", (byte) 1),
    ;

    /**
     * 消息体属性中 第14位
     */
    private final byte versionBit;
    private final String shortDesc;

    Jt808ProtocolVersion(String shortDesc, byte versionBit) {
        this.shortDesc = shortDesc;
        this.versionBit = versionBit;
    }

    public static Jt808ProtocolVersion detectVersion(Jt808MsgHeader header, ByteBuf byteBuf) {
        // bit[14] 0100,0000,0000,0000(4000)(版本标识)
        // 版本标识为1 ==> v2019 才有的
        // 不为1 ==> v2013 或 v2011(v2013 和 v2011没法真正区分出来) ==> 这里默认为 v2013
        if (header.versionIdentifier() != 1) {
            return Jt808ProtocolVersion.VERSION_2013;
        }

        // byte[4] 协议版本号 (header.versionIdentifier() == 1时才有)
        final byte version = byteBuf.getByte(4);
        if (version == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return Jt808ProtocolVersion.VERSION_2019;
        }
        return Jt808ProtocolVersion.VERSION_2013;
    }
}
