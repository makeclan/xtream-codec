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
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hylexus
 */
public interface XtreamByteReader {

    ByteBuf readable();

    static XtreamByteReader of(ByteBuf value) {
        return () -> value;
    }

    static <T> T doWithReader(byte[] bytes, Function<XtreamByteReader, T> fn) {
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer().writeBytes(bytes);
        try {
            return fn.apply(of(buf));
        } finally {
            XtreamBytes.releaseBuf(buf);
        }
    }

    static void doWithReader(byte[] bytes, Consumer<XtreamByteReader> fn) {
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer().writeBytes(bytes);
        try {
            fn.accept(of(buf));
        } finally {
            XtreamBytes.releaseBuf(buf);
        }
    }

    default String readBcd(int length) {
        return XtreamBytes.readBcd(readable(), length);
    }

    default XtreamByteReader readBcd(int length, Consumer<String> consumer) {
        consumer.accept(this.readBcd(length));
        return this;
    }

    default short readU8() {
        return XtreamBytes.readU8(readable());
    }

    default XtreamByteReader readU8(Consumer<Short> consumer) {
        consumer.accept(this.readU8());
        return this;
    }

    default int readU16() {
        return XtreamBytes.readU16(readable());
    }

    default XtreamByteReader readU16(Consumer<Integer> consumer) {
        consumer.accept(this.readU16());
        return this;
    }

    default String readString(int length) {
        return readString(length, StandardCharsets.UTF_8);
    }

    default String readString(int length, Charset charset) {
        return (String) readable().readCharSequence(length, charset);
    }

    default XtreamByteReader readString(int length, Charset charset, Consumer<String> consumer) {
        final String string = readString(length, charset);
        consumer.accept(string);
        return this;
    }

    default XtreamByteReader readString(int length, Consumer<String> consumer) {
        final String string = this.readString(length);
        consumer.accept(string);
        return this;
    }

}
