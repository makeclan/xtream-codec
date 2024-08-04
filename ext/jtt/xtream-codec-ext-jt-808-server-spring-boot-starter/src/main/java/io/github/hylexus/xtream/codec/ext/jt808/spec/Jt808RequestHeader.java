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


import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808MsgBodyProps;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808RequestHeader;

/**
 * @author hylexus
 */
public interface Jt808RequestHeader {

    // region 请求头属性定义

    /**
     * byte[0-2) 消息ID word(16)
     */
    int msgId();

    /**
     * byte[2-4)    消息体属性 word(16)
     */
    Jt808MsgBodyProps msgBodyProps();

    /**
     * {@code v2013}: byte[4-10)    终端手机号或设备ID bcd[6]
     * <p>
     * {@code v2019}: byte[5-15)    终端手机号或设备ID bcd[10]
     */
    String terminalId();

    /**
     * {@code v2013}: byte[10-12) 消息流水号 word(16)
     * <p>
     * {@code v2019}: byte[15-17)    消息流水号 word(16)
     */
    int flowId();

    /**
     * {@code v2013}: byte[12-16)    消息包封装项
     * <p>
     * {@code v2019}: byte[17-21)    消息包封装项
     */
    Jt808SubPackageProps subPackage();
    // endregion 请求头属性定义

    // region 辅助方法定义
    Jt808ProtocolVersion version();

    static int msgBodyStartIndex(Jt808ProtocolVersion version, boolean hasSubPackage) {
        // 2011, 2013
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2013.getVersionBit()) {
            return hasSubPackage ? 16 : 12;
        }
        // 2019
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return hasSubPackage ? 21 : 17;
        }
        throw new IllegalStateException("未知版本, version=" + version);
    }

    default int msgBodyStartIndex() {
        return Jt808RequestHeader.msgBodyStartIndex(version(), msgBodyProps().hasSubPackage());
    }

    default int msgBodyLength() {
        return msgBodyProps().msgBodyLength();
    }

    String toString();

    static Jt808MsgHeaderBuilder newBuilder() {
        return new DefaultJt808RequestHeader.DefaultJt808MsgHeaderBuilder();
    }

    default Jt808MsgHeaderBuilder mutate() {
        return new DefaultJt808RequestHeader.DefaultJt808MsgHeaderBuilder(this);
    }
    // endregion 辅助方法定义

    interface Jt808MsgBodyProps {

        int intValue();

        // bit[0-9] 0000,0011,1111,1111(3FF)(消息体长度)
        default int msgBodyLength() {
            return intValue() & 0x3ff;
        }

        /**
         * bit[10-12] 0001,1100,0000,0000(1C00)(加密类型)
         *
         * @see #dataEncryptionType()
         */
        default int encryptionType() {
            return (intValue() & 0x1c00) >> 10;
        }

        /**
         * @see #encryptionType()
         * @since 2.1.4
         */
        default Jt808MsgEncryptionType dataEncryptionType() {
            return Jt808MsgEncryptionType.fromIntValue(this.encryptionType());
        }

        // bit[13] 0010,0000,0000,0000(2000)(是否有子包)
        default boolean hasSubPackage() {
            return ((intValue() & 0x2000) >> 13) == 1;
        }

        // bit[14] 0100,0000,0000,0000(4000)(版本标识)
        default int versionIdentifier() {
            return (intValue() & 0x4000) >> 14;
        }

        // bit[15] 1000,0000,0000,0000(8000)(保留位)
        default int reversedBit15() {
            return (intValue() & 0x8000) >> 15;
        }

        default Jt808MsgBodyPropsBuilder mutate() {
            return new DefaultJt808MsgBodyProps.DefaultJt808MsgBodyPropsBuilder(this.intValue());
        }
    }

    interface Jt808SubPackageProps {

        /**
         * byte[0-2) 消息包总数(word(16)) 该消息分包后的总包数
         */
        int totalSubPackageCount();

        /**
         * byte[2-4) 包序号(word(16))  从 1 开始
         */
        int currentPackageNo();
    }

    interface Jt808MsgBodyPropsBuilder {

        Jt808MsgBodyPropsBuilder msgBodyLength(int msgBodyLength);

        Jt808MsgBodyPropsBuilder encryptionType(int encryptionType);

        Jt808MsgBodyPropsBuilder hasSubPackage(int subPackageIdentifier);

        Jt808MsgBodyPropsBuilder hasSubPackage(boolean hasSubPackage);

        Jt808MsgBodyPropsBuilder versionIdentifier(int versionIdentifier);

        Jt808MsgBodyPropsBuilder versionIdentifier(Jt808ProtocolVersion version);

        Jt808MsgBodyPropsBuilder reversedBit15(int reversedBit15);

        Jt808MsgBodyProps build();
    }

    interface Jt808MsgHeaderBuilder {
        Jt808MsgHeaderBuilder version(Jt808ProtocolVersion version);

        Jt808MsgHeaderBuilder msgType(int msgType);

        Jt808MsgHeaderBuilder msgBodyProps(Jt808MsgBodyProps msgBodyProps);

        Jt808MsgHeaderBuilder terminalId(String terminalId);

        Jt808MsgHeaderBuilder flowId(int flowId);

        Jt808MsgHeaderBuilder subPackageProps(Jt808SubPackageProps subPackageProps);

        Jt808RequestHeader build();
    }
}
