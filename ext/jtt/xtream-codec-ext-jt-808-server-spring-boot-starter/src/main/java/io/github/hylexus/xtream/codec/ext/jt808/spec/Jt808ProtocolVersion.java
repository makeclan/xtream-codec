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

package io.github.hylexus.xtream.codec.ext.jt808.spec;

public enum Jt808ProtocolVersion {
    /**
     * 自动检测2019版||2013版
     */
    AUTO_DETECTION("ALL", (byte) -1),
    /**
     * 2013 版
     */
    VERSION_2013("2013", (byte) 0),
    /**
     * 2011 版
     */
    VERSION_2011("2011", (byte) 0),
    /**
     * 2019 版
     */
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

    // getters
    public byte getVersionBit() {
        return versionBit;
    }

    public String getShortDesc() {
        return shortDesc;
    }
}
