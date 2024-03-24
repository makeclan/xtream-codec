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
import io.github.hylexus.xtream.codec.core.type.ByteArrayContainer;
import io.github.hylexus.xtream.debug.codec.core.demo02.*;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity02NestedForJunitPurpose;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.hylexus.xtream.debug.codec.core.DebugEntity02Test.generateMsgBodyPropsForJt808;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DebugEntity02NestedTest extends BaseEntityCodecTest {

    RustStyleDebugEntity02ForEncodeNested rustStyleSourceEntityNested = new RustStyleDebugEntity02ForEncodeNested();
    RawStyleDebugEntity02ForEncodeNested rawStyleSourceEntityNested = new RawStyleDebugEntity02ForEncodeNested();
    JtStyleDebugEntity02ForEncodeNested jtStyleSourceEntityNested = new JtStyleDebugEntity02ForEncodeNested();

    @BeforeEach
    void setUp() {
        this.entityCodec = new EntityCodec();
        this.byteBufAllocator = ByteBufAllocator.DEFAULT;

        // init-1
        this.rustStyleSourceEntityNested.setHeader(new RustStyleDebugEntity02ForEncodeNested.Header());
        this.rustStyleSourceEntityNested.setBody(new RustStyleDebugEntity02ForEncodeNested.Body());
        this.resetHeader(this.rustStyleSourceEntityNested.getHeader());
        int bodyLength = this.resetBody(this.rustStyleSourceEntityNested.getBody());
        this.rustStyleSourceEntityNested.getBody().setExtraItems(List.of(
                new RustStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x01, (short) 4, ByteArrayContainer.ofDword(6666).asBytes()),
                new RustStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x02, (short) 2, ByteArrayContainer.ofWord(88).asBytes()),
                new RustStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x03, (short) 2, ByteArrayContainer.ofWord(89).asBytes())
        ));
        bodyLength += this.rustStyleSourceEntityNested.getBody().getExtraItems().stream().mapToInt(it -> 1 + 1 + it.getContent().length).sum();
        this.rustStyleSourceEntityNested.getHeader().setMsgBodyProps(generateMsgBodyPropsForJt808(bodyLength, 0, false, 0));
        this.rustStyleSourceEntityNested.setCheckSum((byte) 111);

        // init-2
        this.rawStyleSourceEntityNested.setHeader(new RawStyleDebugEntity02ForEncodeNested.Header());
        this.rawStyleSourceEntityNested.setBody(new RawStyleDebugEntity02ForEncodeNested.Body());
        this.resetHeader(this.rawStyleSourceEntityNested.getHeader());
        bodyLength = this.resetBody(this.rawStyleSourceEntityNested.getBody());
        this.rawStyleSourceEntityNested.getBody().setExtraItems(List.of(
                new RawStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x01, (short) 4, ByteArrayContainer.ofDword(6666).asBytes()),
                new RawStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x02, (short) 2, ByteArrayContainer.ofWord(88).asBytes()),
                new RawStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x03, (short) 2, ByteArrayContainer.ofWord(89).asBytes())
        ));
        bodyLength += this.rawStyleSourceEntityNested.getBody().getExtraItems().stream().mapToInt(it -> 1 + 1 + it.getContent().length).sum();
        this.rawStyleSourceEntityNested.getHeader().setMsgBodyProps(generateMsgBodyPropsForJt808(bodyLength, 0, false, 0));
        this.rawStyleSourceEntityNested.setCheckSum((byte) 111);

        // init-3
        this.jtStyleSourceEntityNested.setHeader(new JtStyleDebugEntity02ForEncodeNested.Header());
        this.jtStyleSourceEntityNested.setBody(new JtStyleDebugEntity02ForEncodeNested.Body());
        this.resetHeader(this.jtStyleSourceEntityNested.getHeader());
        bodyLength = this.resetBody(this.jtStyleSourceEntityNested.getBody());
        this.jtStyleSourceEntityNested.getBody().setExtraItems(List.of(
                new JtStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x01, (short) 4, ByteArrayContainer.ofDword(6666).asBytes()),
                new JtStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x02, (short) 2, ByteArrayContainer.ofWord(88).asBytes()),
                new JtStyleDebugEntity02ForEncodeNested.ExtraItem((short) 0x03, (short) 2, ByteArrayContainer.ofWord(89).asBytes())
        ));
        bodyLength += this.jtStyleSourceEntityNested.getBody().getExtraItems().stream().mapToInt(it -> 1 + 1 + it.getContent().length).sum();
        this.jtStyleSourceEntityNested.getHeader().setMsgBodyProps(generateMsgBodyPropsForJt808(bodyLength, 0, false, 0));
        this.jtStyleSourceEntityNested.setCheckSum((byte) 111);
    }

    @Test
    void testEncode() {
        final String hexString1 = this.encodeAsHexString(this.rustStyleSourceEntityNested);
        final String hexString2 = this.encodeAsHexString(this.rawStyleSourceEntityNested);
        final String hexString3 = this.encodeAsHexString(this.jtStyleSourceEntityNested);
        assertEquals("0200402a01000000000139111122220063000000580000006f01dc9a0707456246231d029a005a240322222633010400001a0a02020058030200596f", hexString1);
        assertEquals(hexString1, hexString2);
        assertEquals(hexString1, hexString3);
    }

    @Test
    void testDecode() {
        final String hexString = "0200402a01000000000139111122220063000000580000006f01dc9a0707456246231d029a005a240322222633010400001a0a02020058030200596f";

        final RustStyleDebugEntity02ForDecodeNested decodedEntity1 = this.doDecode(RustStyleDebugEntity02ForDecodeNested.class, hexString);
        this.doCompareHeader(this.rustStyleSourceEntityNested.getHeader(), decodedEntity1.getHeader());
        this.doCompareBody(this.rustStyleSourceEntityNested.getBody(), decodedEntity1.getBody());
        this.doCompareExtraItem(this.rustStyleSourceEntityNested.getBody().getExtraItems(), decodedEntity1.getBody().getExtraItems());

        final RawStyleDebugEntity02ForDecodeNested decodedEntity2 = this.doDecode(RawStyleDebugEntity02ForDecodeNested.class, hexString);
        this.doCompareHeader(this.rawStyleSourceEntityNested.getHeader(), decodedEntity2.getHeader());
        this.doCompareBody(this.rawStyleSourceEntityNested.getBody(), decodedEntity2.getBody());
        this.doCompareExtraItem(this.rawStyleSourceEntityNested.getBody().getExtraItems(), decodedEntity2.getBody().getExtraItems());

        final JtStyleDebugEntity02ForDecodeNested decodedEntity3 = this.doDecode(JtStyleDebugEntity02ForDecodeNested.class, hexString);
        this.doCompareHeader(this.jtStyleSourceEntityNested.getHeader(), decodedEntity3.getHeader());
        this.doCompareBody(this.jtStyleSourceEntityNested.getBody(), decodedEntity3.getBody());
        this.doCompareExtraItem(this.jtStyleSourceEntityNested.getBody().getExtraItems(), decodedEntity3.getBody().getExtraItems());
    }

    void resetHeader(DebugEntity02NestedForJunitPurpose.DebugEntity02HeaderForJunitPurpose header) {
        header.setMsgId(0x0200);
        header.setProtocolVersion((byte) 1);
        header.setTerminalId("00000000013911112222");
        header.setMsgSerialNo(99);
        header.setSubPackageInfo(null);
    }

    int resetBody(DebugEntity02NestedForJunitPurpose.DebugEntity02BodyForJunitPurpose body) {
        int msgBodyLength = 0;

        // 报警标志  DWORD(4)
        body.setAlarmFlag(88);
        msgBodyLength += 4;

        // 状态  DWORD(4)
        body.setStatus(111);
        msgBodyLength += 4;

        // 纬度  DWORD(4)
        body.setLatitude(31234567);
        msgBodyLength += 4;

        // 经度  DWORD(4)
        body.setLongitude(121987654);
        msgBodyLength += 4;

        // 高程  WORD(2)
        body.setSpeed(666);
        msgBodyLength += 2;

        // 速度  WORD(2)
        body.setAltitude(8989);
        msgBodyLength += 2;

        // 方向  WORD(2)
        body.setDirection(90);
        msgBodyLength += 2;

        // 时间  BCD[6] yyMMddHHmmss
        body.setTime("240322222633");
        msgBodyLength += 6;

        return msgBodyLength;
    }

    void doCompareHeader(DebugEntity02NestedForJunitPurpose.DebugEntity02HeaderForJunitPurpose header1, DebugEntity02NestedForJunitPurpose.DebugEntity02HeaderForJunitPurpose header2) {
        assertEquals(header1.getMsgId(), header2.getMsgId());
        assertEquals(header1.getMsgBodyProps(), header2.getMsgBodyProps());
        assertEquals(header1.getProtocolVersion(), header2.getProtocolVersion());
        assertEquals(header1.getTerminalId(), header2.getTerminalId());
        assertEquals(header1.getMsgSerialNo(), header2.getMsgSerialNo());
        assertEquals(header1.getSubPackageInfo(), header2.getSubPackageInfo());
    }

    void doCompareBody(DebugEntity02NestedForJunitPurpose.DebugEntity02BodyForJunitPurpose entity1, DebugEntity02NestedForJunitPurpose.DebugEntity02BodyForJunitPurpose entity2) {
        assertEquals(entity1.getAlarmFlag(), entity2.getAlarmFlag());
        assertEquals(entity1.getStatus(), entity2.getStatus());
        assertEquals(entity1.getLatitude(), entity2.getLatitude());
        assertEquals(entity1.getLongitude(), entity2.getLongitude());
        assertEquals(entity1.getAltitude(), entity2.getAltitude());
        assertEquals(entity1.getSpeed(), entity2.getSpeed());
        assertEquals(entity1.getDirection(), entity2.getDirection());
        assertEquals(entity1.getTime(), entity2.getTime());
    }


    public void doCompareExtraItem(List<? extends DebugEntity02NestedForJunitPurpose.DebugEntity02ExtraItemForJunitPurpose> item1, List<? extends DebugEntity02NestedForJunitPurpose.DebugEntity02ExtraItemForJunitPurpose> item2) {
        assertEquals(item1.size(), item2.size());
        for (int i = 0; i < item1.size(); i++) {
            final DebugEntity02NestedForJunitPurpose.DebugEntity02ExtraItemForJunitPurpose left = item1.get(i);
            final DebugEntity02NestedForJunitPurpose.DebugEntity02ExtraItemForJunitPurpose right = item2.get(i);
            assertEquals(left.getId(), right.getId());
            assertEquals(left.getContentLength(), right.getContentLength());
            assertArrayEquals(left.getContent(), right.getContent());
        }
    }

}
