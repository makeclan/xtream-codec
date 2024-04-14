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

import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.debug.codec.core.demo03.RustStyleDebugEntity03ForDecode;
import io.github.hylexus.xtream.debug.codec.core.demo03.RustStyleDebugEntity03ForEncode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DebugEntity03Test extends BaseEntityCodecTest {

    @BeforeEach
    void setUp() {
        this.entityCodec = new EntityCodec();
    }

    @Test
    void test() {
        final RustStyleDebugEntity03ForEncode entity = new RustStyleDebugEntity03ForEncode();
        entity.setMsgId(123);

        final Map<Integer, Object> attr = new LinkedHashMap<>();
        attr.put(1, "abcd");
        attr.put(2, "哈哈 123");
        attr.put(3, (short) 111);
        attr.put(4, "...。。。");
        entity.setAttr(attr);

        // System.out.println(entity);
        final String hexString = encodeAsHexString(entity);
        // System.out.println(hexString);

        final RustStyleDebugEntity03ForDecode instance = doDecode(RustStyleDebugEntity03ForDecode.class, hexString);

        assertEquals(entity.getMsgId(), instance.getMsgId());
        assertEquals(attr.get(1), instance.getAttr().get(1));
        assertEquals(attr.get(2), instance.getAttr().get(2));
        assertEquals(attr.get(3), instance.getAttr().get(3));
        assertArrayEquals(((String) attr.get(4)).getBytes(), ((byte[]) instance.getAttr().get(4)));
        // System.out.println(instance);
    }
}
