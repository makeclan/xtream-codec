/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.debug.codec.core;

import io.github.hylexus.xtream.codec.common.utils.XtreamConstants;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.debug.codec.core.demo01.*;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity01ForJunitPurpose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DebugEntity01Test extends BaseEntityCodecTest {

    RustStyleDebugEntity01ForEncode rustStyleSourceEntity = new RustStyleDebugEntity01ForEncode();
    JtStyleDebugEntity01ForEncode jtStyleSourceEntity = new JtStyleDebugEntity01ForEncode();
    RawStyleDebugEntity01ForEncode rawStyleSourceEntity = new RawStyleDebugEntity01ForEncode();

    @BeforeEach
    void setUp() {
        this.entityCodec = EntityCodec.DEFAULT;
        this.resetEntityProperties(this.rustStyleSourceEntity);
        this.resetEntityProperties(this.jtStyleSourceEntity);
        this.resetEntityProperties(this.rawStyleSourceEntity);
    }

    // @Test
    // void testEncode1() {
    //     final EntityCodec entityCodec = new EntityCodec();
    //
    //     final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
    //     try {
    //         final RustStyleDebugEntity01ForEncode instance = new RustStyleDebugEntity01ForEncode();
    //         // 省略属性赋值
    //         // instance.setXxx(someValue);
    //         // ...
    //         instance.setMajorVersion((short) 1);
    //
    //         // 将 instance 的数据序列化到 buffer 中
    //         entityCodec.encode(instance, buffer);
    //         // 使用 buffer
    //         System.out.println(ByteBufUtil.hexDump(buffer));
    //     } finally {
    //         buffer.release();
    //     }
    // }
    //
    // @Test
    // void testDecode1() {
    //     final EntityCodec entityCodec = new EntityCodec();
    //
    //     // buffer 中存储的是要反序列化的数据(这里写死用来演示)
    //     final String hexString = "8090123401020001003d001678747265616d2d636f6465632ee794a8e688b7e5908d001178747265616d2d636f6465632ec3dcc2eb3230323130323033013911112222270fff9c";
    //     final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer().writeBytes(XtreamBytes.decodeHex(hexString));
    //
    //     try {
    //         final RustStyleDebugEntity01ForDecode entity = entityCodec.decode(RustStyleDebugEntity01ForDecode.class, buffer);
    //         System.out.println(entity);
    //     } finally {
    //         buffer.release();
    //     }
    // }

    @Test
    void testEncode() {
        final String hexString1 = this.encodeAsHexString(this.rustStyleSourceEntity);
        final String hexString2 = this.encodeAsHexString(this.jtStyleSourceEntity);
        final String hexString3 = this.encodeAsHexString(this.rawStyleSourceEntity);

        assertEquals("8090123401020007003d001678747265616d2d636f6465632ee794a8e688b7e5908d001178747265616d2d636f6465632ec3dcc2eb3230323130323033013911112222270fff9c", hexString1);
        assertEquals(hexString1, hexString2);
        assertEquals(hexString1, hexString3);
    }


    @Test
    void testDecode() {
        final String hexString = "8090123401020007003d001678747265616d2d636f6465632ee794a8e688b7e5908d001178747265616d2d636f6465632ec3dcc2eb3230323130323033013911112222270fff9c";

        final RustStyleDebugEntity01ForDecode decodedEntity1 = this.doDecode(RustStyleDebugEntity01ForDecode.class, hexString);
        final JtStyleDebugEntity01ForDecode decodedEntity2 = this.doDecode(JtStyleDebugEntity01ForDecode.class, hexString);
        final RawStyleDebugEntity01ForDecode decodedEntity3 = this.doDecode(RawStyleDebugEntity01ForDecode.class, hexString);
        System.out.println(decodedEntity1);
        System.out.println(decodedEntity2);
        System.out.println(decodedEntity3);
        this.doCompare(this.jtStyleSourceEntity, decodedEntity2);
        this.doCompare(decodedEntity1, decodedEntity2);
        this.doCompare(decodedEntity1, decodedEntity3);
    }

    void doCompare(DebugEntity01ForJunitPurpose entity1, DebugEntity01ForJunitPurpose entity2) {
        assertEquals(entity1.getMagicNumber(), entity2.getMagicNumber());
        assertEquals(entity1.getMajorVersion(), entity2.getMajorVersion());
        assertEquals(entity1.getMinorVersion(), entity2.getMinorVersion());
        assertEquals(entity1.getMsgBodyLength(), entity2.getMsgBodyLength());
        assertEquals(entity1.getMsgType(), entity2.getMsgType());
        assertEquals(entity1.getUsernameLength(), entity2.getUsernameLength());
        assertEquals(entity1.getUsername(), entity2.getUsername());
        assertEquals(entity1.getPasswordLength(), entity2.getPasswordLength());
        assertEquals(entity1.getPassword(), entity2.getPassword());
        assertEquals(entity1.getPhoneNumber(), entity2.getPhoneNumber());
        assertEquals(entity1.getBirthday(), entity2.getBirthday());
        assertEquals(entity1.getAge(), entity2.getAge());
        assertEquals(entity1.getStatus(), entity2.getStatus());
    }

    // 返回一个填充了属性的 DebugEntity01ForEncode 对象
    void resetEntityProperties(DebugEntity01ForJunitPurpose entity) {

        // region header
        entity.setMagicNumber(0x80901234);
        entity.setMajorVersion((short) 1);
        entity.setMinorVersion((short) 2);
        entity.setMsgType(0x0007);
        // endregion header

        // region body
        int msgBodyLength = 0;

        final String username = "xtream-codec.用户名";
        entity.setUsername(username);
        entity.setUsernameLength(username.getBytes(StandardCharsets.UTF_8).length);
        msgBodyLength += entity.getUsernameLength() + 2;

        final String password = "xtream-codec.密码";
        entity.setPassword(password);
        entity.setPasswordLength(password.getBytes(XtreamConstants.CHARSET_GBK).length);
        msgBodyLength += entity.getPasswordLength() + 2;

        entity.setBirthday("20210203");
        msgBodyLength += 8;

        entity.setPhoneNumber("013911112222");
        msgBodyLength += 6;

        entity.setAge(9999);
        msgBodyLength += 2;

        entity.setStatus((short) -100);
        msgBodyLength += 2;

        entity.setMsgBodyLength(msgBodyLength);
        // endregion body
    }

}
