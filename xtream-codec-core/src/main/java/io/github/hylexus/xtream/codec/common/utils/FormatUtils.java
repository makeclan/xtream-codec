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

package io.github.hylexus.xtream.codec.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * Copied from jt-framework.
 *
 * @author hylexus
 */
public final class FormatUtils {
    private FormatUtils() {
    }

    public static String toBinaryString(int i) {
        return Integer.toBinaryString(i);
    }

    public static String toBinaryString(int i, int minLen) {
        final String binaryString = Integer.toBinaryString(i);
        final int prefixLen = Math.max(minLen - binaryString.length(), 0);
        return "0".repeat(prefixLen) + binaryString;
    }

    public static String toHexString(int i) {
        return Integer.toHexString(i);
    }

    public static String toHexString(int i, int minLen) {
        final String hexString = Integer.toHexString(i);
        final int prefixLen = Math.max(minLen - hexString.length(), 0);
        return "0".repeat(prefixLen) + hexString;
    }

    public static String toHexString(ByteBuf byteBuf) {
        return ByteBufUtil.hexDump(byteBuf);
    }

    public static String toHexString(byte[] bytes) {
        return ByteBufUtil.hexDump(bytes);
    }
}
