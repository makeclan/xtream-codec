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

package io.github.hylexus.xtream.debug.codec.core;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.debug.codec.core.democustomannotation.CustomAnnotationDebugEntity01;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CustomAnnotationTest {

    final EntityCodec entityCodec = new EntityCodec();

    @Test
    void testEncode() {

        final CustomAnnotationDebugEntity01 originalInstance = new CustomAnnotationDebugEntity01();
        originalInstance.setMajorVersion((short) 2);
        originalInstance.setBirthday(LocalDate.of(2024, 2, 10));

        // 编码/序列化
        final String hexString = encode(originalInstance);

        // 解码/反序列化
        final CustomAnnotationDebugEntity01 decodedInstance = decode(hexString);

        Assertions.assertEquals(originalInstance.getMajorVersion(), decodedInstance.getMajorVersion());
        Assertions.assertEquals(originalInstance.getBirthday(), decodedInstance.getBirthday());
    }

    private CustomAnnotationDebugEntity01 decode(String hexString) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer().writeBytes(XtreamBytes.decodeHex(hexString));
        try {
            return entityCodec.decode(CustomAnnotationDebugEntity01.class, buffer);
        } finally {
            buffer.release();
        }
    }

    private String encode(CustomAnnotationDebugEntity01 originalInstance) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            entityCodec.encode(originalInstance, buffer);
            return ByteBufUtil.hexDump(buffer);
        } finally {
            buffer.release();
        }
    }
}
