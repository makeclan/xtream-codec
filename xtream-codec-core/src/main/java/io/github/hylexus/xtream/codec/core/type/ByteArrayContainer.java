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

package io.github.hylexus.xtream.codec.core.type;


import io.github.hylexus.xtream.codec.common.utils.BcdOps;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Copied from jt-framework.
 *
 * @author hylexus
 */
public interface ByteArrayContainer extends BytesContainer {

    int MASK = 0xFF;

    static ByteArrayContainer ofBytes(byte[] bytes) {
        return () -> bytes;
    }

    static ByteArrayContainer ofI8(int value) {
        return () -> new byte[]{(byte) value};
    }

    static ByteArrayContainer ofU8(int value) {
        return ofI8(value);
    }

    static ByteArrayContainer ofU16(int value) {
        return ofI16(value);
    }

    static ByteArrayContainer ofI16(int value) {
        return () -> new byte[]{
                (byte) (value >>> 8),
                (byte) value
        };
    }

    static ByteArrayContainer ofI32(int value) {
        return () -> new byte[]{
                (byte) ((value >>> 24) & MASK),
                (byte) ((value >>> 16) & MASK),
                (byte) ((value >>> 8) & MASK),
                (byte) (value & MASK)
        };
    }

    static ByteArrayContainer ofU32(int value) {
        return ofI32(value);
    }

    static ByteArrayContainer ofByte(int value) {
        return ofU8(value);
    }

    static ByteArrayContainer ofWord(int value) {
        return ofU16(value);
    }

    static ByteArrayContainer ofDword(int i) {
        return ofU32(i);
    }

    static ByteArrayContainer ofBcd(String bcd) {
        return () -> BcdOps.encodeBcd8421AsBytes(bcd);
    }

    static ByteArrayContainer ofString(String string, Charset charset) {
        return () -> string.getBytes(charset);
    }

    static ByteArrayContainer ofString(String string) {
        return ofString(string, StandardCharsets.UTF_8);
    }

    byte[] payload();

    @Override
    default int length() {
        return this.payload().length;
    }

    @Override
    default byte[] asBytes() {
        return this.payload();
    }

    @Override
    default String asString(Charset charset) {
        return new String(this.payload(), charset);
    }

    default byte asI8() {
        return this.payload()[0];
    }

    default short asI16() {
        final byte[] value = this.payload();
        return (short) ((value[0] & 0xff) << 8 | value[1] & 0xff);
    }

    default int asI32() {
        final byte[] bytes = this.payload();
        return ((bytes[0] & MASK) << 24)
                |
                ((bytes[1] & MASK) << 16)
                |
                ((bytes[2] & MASK) << 8)
                |
                ((bytes[3] & MASK));
    }
}
