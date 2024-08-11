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


import io.github.hylexus.xtream.codec.common.utils.FormatUtils;

public interface Jt808MessageEncryptionType {

    int DEFAULT_ENCRYPTION_TYPE = 0b000;

    static Jt808MessageEncryptionType fromMessageBodyProps(int bodyPros) {
        // bit[10-12] 0001,1100,0000,0000(1C00)(加密类型)
        return new Default((bodyPros & 0x1c00) >> 10);
    }

    static Jt808MessageEncryptionType fromIntValue(int value) {
        return new Default(value & 0b111);
    }

    static Jt808MessageEncryptionType fromBits(int bit10, int bit11, int bit12) {
        return new Default(
                (bit10 & 0b1)
                        | ((bit11 << 1) & 0b10)
                        | ((bit12 << 2) & 0b100)
        );
    }

    int intValue();

    default int bit10() {
        return this.intValue() & 0b001;
    }

    default int bit11() {
        return this.intValue() & 0b010;
    }

    default int bit12() {
        return this.intValue() & 0b100;
    }

    default boolean isEncrypted() {
        return this.intValue() != 0;
    }

    class Default implements Jt808MessageEncryptionType {
        private final int value;

        public Default(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Default{"
                    + "value=" + value
                    + "(" + FormatUtils.toBinaryString(value, 3) + ")"
                    + '}';
        }
    }
}
