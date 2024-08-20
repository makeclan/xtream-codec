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

import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.debug.codec.core.demo01.*;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity01ForJunitPurpose;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity01NestedForJunitPurpose;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DebugEntity01NestedTest extends BaseEntityCodecTest {

    RustStyleDebugEntity01ForEncodeNested rustStyleSourceEntityNested = new RustStyleDebugEntity01ForEncodeNested();
    RawStyleDebugEntity01ForEncodeNested rawStyleSourceEntityNested = new RawStyleDebugEntity01ForEncodeNested();
    JtStyleDebugEntity01ForEncodeNested jtStyleSourceEntityNested = new JtStyleDebugEntity01ForEncodeNested();

    @BeforeEach
    void setUp() {
        this.entityCodec = new EntityCodec();
        this.byteBufAllocator = ByteBufAllocator.DEFAULT;

        // init-1
        this.rustStyleSourceEntityNested.setHeader(new RustStyleDebugEntity01ForEncodeNested.Header());
        this.rustStyleSourceEntityNested.setBody(new RustStyleDebugEntity01ForEncodeNested.Body());
        this.resetHeader(this.rustStyleSourceEntityNested.getHeader());
        int bodyLength = this.resetBody(this.rustStyleSourceEntityNested.getBody());
        this.rustStyleSourceEntityNested.setMsgBodyLength(bodyLength);

        // init-2
        this.rawStyleSourceEntityNested.setHeader(new RawStyleDebugEntity01ForEncodeNested.Header());
        this.rawStyleSourceEntityNested.setBody(new RawStyleDebugEntity01ForEncodeNested.Body());
        this.resetHeader(this.rawStyleSourceEntityNested.getHeader());
        bodyLength = this.resetBody(this.rawStyleSourceEntityNested.getBody());
        this.rawStyleSourceEntityNested.setMsgBodyLength(bodyLength);

        // init-3
        this.jtStyleSourceEntityNested.setHeader(new JtStyleDebugEntity01ForEncodeNested.Header());
        this.jtStyleSourceEntityNested.setBody(new JtStyleDebugEntity01ForEncodeNested.Body());
        this.resetHeader(this.jtStyleSourceEntityNested.getHeader());
        bodyLength = this.resetBody(this.jtStyleSourceEntityNested.getBody());
        this.jtStyleSourceEntityNested.setMsgBodyLength(bodyLength);
    }

    @Test
    void testEncode() {
        final String hexString1 = this.encodeAsHexString(this.rustStyleSourceEntityNested);
        final String hexString2 = this.encodeAsHexString(this.rawStyleSourceEntityNested);
        final String hexString3 = this.encodeAsHexString(this.jtStyleSourceEntityNested);
        assertEquals("809012340203000700420016e5bca0e4b889e4b8b0205554462d3820e7bc96e7a081001670617373776f72642dc3dcc2eb2d42474b20b1e0c2eb3230323430323130013900001111270fff9c", hexString1);
        assertEquals(hexString1, hexString2);
        assertEquals(hexString1, hexString3);
    }

    @Test
    void testDecode() {
        final String hexString = "809012340203000700420016e5bca0e4b889e4b8b0205554462d3820e7bc96e7a081001670617373776f72642dc3dcc2eb2d42474b20b1e0c2eb3230323430323130013900001111270fff9c";

        final RustStyleDebugEntity01ForDecodeNested decodedEntity1 = this.doDecode(RustStyleDebugEntity01ForDecodeNested.class, hexString);
        this.doCompareHeader(this.rustStyleSourceEntityNested.getHeader(), decodedEntity1.getHeader());
        this.doCompareBody(this.rustStyleSourceEntityNested.getBody(), decodedEntity1.getBody());
        assertEquals(this.rustStyleSourceEntityNested.getMsgBodyLength(), decodedEntity1.getMsgBodyLength());

        final RawStyleDebugEntity01ForDecodeNested decodedEntity2 = this.doDecode(RawStyleDebugEntity01ForDecodeNested.class, hexString);
        this.doCompareHeader(this.rustStyleSourceEntityNested.getHeader(), decodedEntity2.getHeader());
        this.doCompareBody(this.rustStyleSourceEntityNested.getBody(), decodedEntity2.getBody());
        assertEquals(this.rustStyleSourceEntityNested.getMsgBodyLength(), decodedEntity2.getMsgBodyLength());

        final JtStyleDebugEntity01ForDecodeNested decodedEntity3 = this.doDecode(JtStyleDebugEntity01ForDecodeNested.class, hexString);
        this.doCompareHeader(this.jtStyleSourceEntityNested.getHeader(), decodedEntity3.getHeader());
        this.doCompareBody(this.jtStyleSourceEntityNested.getBody(), decodedEntity3.getBody());
        assertEquals(this.jtStyleSourceEntityNested.getMsgBodyLength(), decodedEntity3.getMsgBodyLength());
    }

    void resetHeader(DebugEntity01NestedForJunitPurpose.DebugEntity01NestedHeader header) {
        header.setMagicNumber(0x80901234);
        header.setMajorVersion((short) 2);
        header.setMinorVersion((short) 3);
        header.setMsgType(0x0007);
    }

    int resetBody(DebugEntity01NestedForJunitPurpose.DebugEntity01NestedBody body) {
        int msgBodyLength = 0;

        final String username = "张三丰 UTF-8 编码";
        body.setUsername(username);
        body.setUsernameLength(username.getBytes(StandardCharsets.UTF_8).length);
        msgBodyLength += 2 + body.getUsernameLength();

        final String password = "password-密码-BGK 编码";
        body.setPassword(password);
        body.setPasswordLength(password.getBytes(Charset.forName("GBK")).length);
        msgBodyLength += 2 + body.getPasswordLength();

        body.setBirthday("20240210");
        msgBodyLength += 8;

        body.setPhoneNumber("013900001111");
        msgBodyLength += 6;

        body.setAge(9999);
        msgBodyLength += 2;

        body.setStatus((short) -100);
        msgBodyLength += 2;

        return msgBodyLength;
    }

    void doCompareHeader(DebugEntity01NestedForJunitPurpose.DebugEntity01NestedHeader header1, DebugEntity01NestedForJunitPurpose.DebugEntity01NestedHeader header2) {
        assertEquals(header1.getMagicNumber(), header2.getMagicNumber());
        assertEquals(header1.getMajorVersion(), header2.getMajorVersion());
        assertEquals(header1.getMinorVersion(), header2.getMinorVersion());
        assertEquals(header1.getMsgType(), header2.getMsgType());
    }

    void doCompareBody(DebugEntity01NestedForJunitPurpose.DebugEntity01NestedBody entity1, DebugEntity01NestedForJunitPurpose.DebugEntity01NestedBody entity2) {
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
        entity.setPasswordLength(password.getBytes(Charset.forName("GBK")).length);
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
