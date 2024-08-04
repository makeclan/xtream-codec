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

package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.common.utils.XtreamUtils;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;

/**
 * @author hylexus
 */
public class DefaultJt808MsgBodyProps implements Jt808RequestHeader.Jt808MsgBodyProps {

    private final int intValue;

    public DefaultJt808MsgBodyProps(int intValue) {
        this.intValue = intValue;
    }

    @Override
    public int intValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return "MsgBodyProps{"
                + "intValue=" + intValue
                + ", msgBodyLength=" + msgBodyLength()
                + ", hasSubPackage=" + hasSubPackage()
                + ", encryptionType=" + encryptionType()
                + '}';
    }

    public static class DefaultJt808MsgBodyPropsBuilder implements Jt808RequestHeader.Jt808MsgBodyPropsBuilder {
        private int intValue;

        public DefaultJt808MsgBodyPropsBuilder() {
        }

        public DefaultJt808MsgBodyPropsBuilder(int intValue) {
            this.intValue = intValue;
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder msgBodyLength(int msgBodyLength) {
            this.intValue = XtreamUtils.setBitRange(msgBodyLength, 10, intValue, 0);
            return this;
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder encryptionType(int encryptionType) {
            this.intValue = XtreamUtils.setBitRange(encryptionType, 3, intValue, 10);
            return this;
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder hasSubPackage(int subPackageIdentifier) {
            this.intValue = XtreamUtils.setBitRange(subPackageIdentifier, 1, intValue, 13);
            return this;
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder hasSubPackage(boolean hasSubPackage) {
            return this.hasSubPackage(hasSubPackage ? 1 : 0);
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder versionIdentifier(int versionIdentifier) {
            this.intValue = XtreamUtils.setBitRange(versionIdentifier, 1, intValue, 14);
            return this;
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder versionIdentifier(Jt808ProtocolVersion version) {
            return this.versionIdentifier(version.getVersionBit());
        }

        @Override
        public Jt808RequestHeader.Jt808MsgBodyPropsBuilder reversedBit15(int reversedBit15) {
            this.intValue = XtreamUtils.setBitRange(reversedBit15, 1, intValue, 15);
            return this;
        }

        public Jt808RequestHeader.Jt808MsgBodyProps build() {
            return new DefaultJt808MsgBodyProps(intValue);
        }
    }
}
