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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseEntityCodecTest {

    EntityCodec entityCodec;
    ByteBufAllocator byteBufAllocator = ByteBufAllocator.DEFAULT;

    protected String encodeAsHexString(Object entity) {
        final ByteBuf buffer = this.byteBufAllocator.buffer();
        try {
            this.entityCodec.encode(entity, buffer);
            return ByteBufUtil.hexDump(buffer);
        } finally {
            buffer.release();
        }
    }

    protected <T> T doDecode(Class<T> cls, String hexString) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer().writeBytes(XtreamBytes.decodeHex(hexString));
        try {
            return entityCodec.decode(cls, buffer);
        } finally {
            buffer.release();
            assertEquals(0, buffer.refCnt());
        }
    }
}
