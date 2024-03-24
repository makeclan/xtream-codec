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

/**
 * Copied from jt-framework.
 *
 * @author hylexus
 */
public abstract class BcdOps {

    public static String decodeBcd8421AsString(ByteBuf source, final int length) {
        final StringBuilder builder = new StringBuilder(length << 2);
        for (int i = 0; i < length; i++) {
            final byte value = source.readByte();
            builder.append((value & 0xf0) >>> 4);
            builder.append(value & 0x0f);
        }
        return builder.toString();
    }

    public static String decodeBcd8421AsString(final byte[] source, final int start, final int end) {
        assert start < end : "start < end";

        final int length = end - start;
        final StringBuilder builder = new StringBuilder(length << 2);
        for (int i = start; i < end; i++) {
            builder.append((source[i] & 0xf0) >>> 4);
            builder.append(source[i] & 0x0f);
        }
        return builder.toString();
    }

    public static void encodeBcd8421StringIntoByteBuf(final String str, ByteBuf target) {
        // odd -> 0 + str
        final String source = (str.length() & 1) == 1
                ? "0" + str
                : str;

        final int length = source.length() >> 1;

        final byte[] sourceBytes = source.getBytes();
        for (int i = 0; i < length; i++) {

            byte high = asciiToBcd(sourceBytes[i << 1]);
            byte low = asciiToBcd(sourceBytes[(i << 1) + 1]);

            byte value = (byte) ((high << 4) | low);

            target.writeByte(value);
        }
    }

    public static byte[] encodeBcd8421AsBytes(final String str) {
        // odd -> 0 + str
        final String source = (str.length() & 1) == 1
                ? "0" + str
                : str;

        final byte[] result = new byte[source.length() >> 1];
        final byte[] sourceBytes = source.getBytes();
        for (int i = 0; i < result.length; i++) {

            byte high = asciiToBcd(sourceBytes[i << 1]);
            byte low = asciiToBcd(sourceBytes[(i << 1) + 1]);

            result[i] = (byte) ((high << 4) | low);
        }
        return result;
    }

    private static byte asciiToBcd(final byte asc) {
        if ((asc >= '0') && (asc <= '9')) {
            return (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            return (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            return (byte) (asc - 'a' + 10);
        } else {
            return (byte) (asc - 48);
        }
    }
}
