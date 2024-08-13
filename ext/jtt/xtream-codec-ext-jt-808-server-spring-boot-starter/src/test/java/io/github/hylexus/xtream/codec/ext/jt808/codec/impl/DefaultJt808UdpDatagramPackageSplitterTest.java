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

package io.github.hylexus.xtream.codec.ext.jt808.codec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultJt808UdpDatagramPackageSplitterTest {
    private DefaultJt808UdpDatagramPackageSplitter splitter;

    @BeforeEach
    public void setUp() {
        splitter = new DefaultJt808UdpDatagramPackageSplitter();
    }

    @Test
    public void test1() {

        final List<String> hexStringList = List.of(
                "7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E",
                "7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E",
                "7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E"
        );

        final ByteBufAllocator bufferFactory = ByteBufAllocator.DEFAULT;
        final ByteBuf byteBuf = XtreamBytes.byteBufFromHexString(bufferFactory, String.join("", hexStringList));
        final List<ByteBuf> byteBufList = splitter.split(byteBuf);
        assertEquals(1 + byteBufList.size(), byteBuf.refCnt());
        for (int i = 0; i < byteBufList.size(); i++) {
            final ByteBuf buffer = byteBufList.get(i);
            assertEquals(hexStringList.get(i).toUpperCase(), ("7E" + FormatUtils.toHexString(buffer) + "7E").toUpperCase());
            assertEquals(1, buffer.refCnt());
            buffer.release();
        }
    }

    @Test
    void test2() {
        final String hexString = "7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E";
        final ByteBuf source = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hexString);
        final List<ByteBuf> byteBufList = splitter.split(source);
        assertEquals(1, byteBufList.size());
        assertEquals(2, source.refCnt());
        assertEquals(1, byteBufList.getFirst().refCnt());
        assertEquals(hexString, ("7E" + FormatUtils.toHexString(byteBufList.getFirst()) + "7E").toUpperCase());
        byteBufList.getFirst().release();
    }

    @Test
    void test3() {
        final String hexString = "010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E";
        final ByteBuf source = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hexString);
        final List<ByteBuf> byteBufList = splitter.split(source);
        assertEquals(1, byteBufList.size());
        assertEquals(2, source.refCnt());
        assertEquals(1, byteBufList.getFirst().refCnt());
        assertEquals(hexString, (FormatUtils.toHexString(byteBufList.getFirst()) + "7E").toUpperCase());
        byteBufList.getFirst().release();
    }

    @Test
    void test4() {
        final String hexString = "010060240100000000013912344329000000030001000B0002696439383736353433323174797065303031323334353637383132333435363727";
        final ByteBuf source = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hexString);
        final List<ByteBuf> byteBufList = splitter.split(source);
        assertEquals(1, byteBufList.size());
        assertEquals(2, source.refCnt());
        assertEquals(1, byteBufList.getFirst().refCnt());
        assertEquals(hexString, FormatUtils.toHexString(byteBufList.getFirst()).toUpperCase());
        byteBufList.getFirst().release();
    }

}
