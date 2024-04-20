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
 * @author hylexus
 */
public class XtreamBytes {

    private static final char[] DIGITS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encodeHex(ByteBuf byteBuf) {
        final StringBuilder builder = new StringBuilder();
        int readableBytes = byteBuf.readableBytes();
        for (int i = 0; i < readableBytes; i++) {
            final byte b = byteBuf.getByte(i);
            builder.append(DIGITS_HEX[(0xF0 & b) >>> 4])
                    .append(DIGITS_HEX[0x0F & b]);
        }
        return builder.toString();
    }

    public static String encodeHex(ByteBuf byteBuf, int start, int length) {
        final StringBuilder builder = new StringBuilder();

        for (int i = start; i < length; i++) {
            final byte b = byteBuf.getByte(i);
            builder.append(DIGITS_HEX[(0xF0 & b) >>> 4])
                    .append(DIGITS_HEX[0x0F & b]);
        }
        return builder.toString();
    }

    public static byte[] decodeHex(String hex) {
        return decodeHex(hex.toCharArray());
    }

    public static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }


    public static byte[] readBytes(ByteBuf byteBuf, int length) {
        final byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static byte[] prefixZero(byte[] bytes, int length) {
        if (bytes.length >= length) {
            return bytes;
        }
        final byte[] newBytes = new byte[length];
        System.arraycopy(bytes, 0, newBytes, length - bytes.length, bytes.length);
        return newBytes;
    }

    public static short readU8(ByteBuf readable) {
        return readable.readUnsignedByte();
    }

    public static int readU16(ByteBuf readable) {
        return readable.readUnsignedShort();
    }

    public static String readBcd(ByteBuf readable, int length) {
        return BcdOps.decodeBcd8421AsString(readable, length);
    }
}
