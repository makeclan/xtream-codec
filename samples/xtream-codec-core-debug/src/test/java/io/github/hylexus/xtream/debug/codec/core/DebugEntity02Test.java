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
import io.github.hylexus.xtream.codec.core.type.ByteArrayContainer;
import io.github.hylexus.xtream.debug.codec.core.demo02.*;
import io.github.hylexus.xtream.debug.codec.core.utilsforunittest.DebugEntity02ForJunitPurpose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DebugEntity02Test extends BaseEntityCodecTest {

    RustStyleDebugEntity02ForEncode rustStyleSourceEntity = new RustStyleDebugEntity02ForEncode();
    JtStyleDebugEntity02ForEncode jtStyleSourceEntity = new JtStyleDebugEntity02ForEncode();
    RawStyleDebugEntity02ForEncode rawStyleSourceEntity = new RawStyleDebugEntity02ForEncode();

    static int generateMsgBodyPropsForJt808(int msgBodySize, int encryptionType, boolean isSubPackage, int reversedBit15) {
        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        int props = (msgBodySize & 0x3FF)
                // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
                | ((encryptionType << 10) & 0x1C00)
                // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
                | (((isSubPackage ? 1 : 0) << 13) & 0x2000)
                // [14_ ]  0100,0000,0000,0000(4000)(保留位)
                // 1: V2019 固定为 1
                | ((1 << 14) & 0x4000)
                // [15_ ]  1000,0000,0000,0000(8000)(保留位)
                | ((reversedBit15 << 15) & 0x8000);
        return props & 0xFFFF;
    }

    @BeforeEach
    void setUp() {
        this.entityCodec = EntityCodec.DEFAULT;

        // init-1
        this.resetEntityBasicProperties(this.rustStyleSourceEntity);
        this.rustStyleSourceEntity.setExtraItems(List.of(
                new RustStyleDebugEntity02ForEncode.ExtraItem((short) 0x01, (short) 4, ByteArrayContainer.ofDword(6666).asBytes()),
                new RustStyleDebugEntity02ForEncode.ExtraItem((short) 0x02, (short) 2, ByteArrayContainer.ofI16(88).asBytes()),
                new RustStyleDebugEntity02ForEncode.ExtraItem((short) 0x03, (short) 2, ByteArrayContainer.ofI16(89).asBytes())
        ));
        this.setMsgBodyProps(this.rustStyleSourceEntity, this.rustStyleSourceEntity.getExtraItems());


        // init-2
        this.resetEntityBasicProperties(this.jtStyleSourceEntity);
        this.jtStyleSourceEntity.setExtraItems(List.of(
                new JtStyleDebugEntity02ForEncode.ExtraItem((short) 0x01, (short) 4, ByteArrayContainer.ofDword(6666).asBytes()),
                new JtStyleDebugEntity02ForEncode.ExtraItem((short) 0x02, (short) 2, ByteArrayContainer.ofI16(88).asBytes()),
                new JtStyleDebugEntity02ForEncode.ExtraItem((short) 0x03, (short) 2, ByteArrayContainer.ofI16(89).asBytes())
        ));
        this.setMsgBodyProps(this.jtStyleSourceEntity, this.jtStyleSourceEntity.getExtraItems());

        // init-3
        this.resetEntityBasicProperties(this.rawStyleSourceEntity);
        this.rawStyleSourceEntity.setExtraItems(List.of(
                new RawStyleDebugEntity02ForEncode.ExtraItem((short) 0x01, (short) 4, ByteArrayContainer.ofDword(6666).asBytes()),
                new RawStyleDebugEntity02ForEncode.ExtraItem((short) 0x02, (short) 2, ByteArrayContainer.ofI16(88).asBytes()),
                new RawStyleDebugEntity02ForEncode.ExtraItem((short) 0x03, (short) 2, ByteArrayContainer.ofI16(89).asBytes())
        ));
        this.setMsgBodyProps(this.rawStyleSourceEntity, this.rawStyleSourceEntity.getExtraItems());
    }

    @Test
    void testEncode() {
        final String hexString1 = this.encodeAsHexString(this.rustStyleSourceEntity);
        final String hexString2 = this.encodeAsHexString(this.jtStyleSourceEntity);
        final String hexString3 = this.encodeAsHexString(this.rawStyleSourceEntity);

        assertEquals("0200402a01000000000139111122220063000000580000006f01dc9a0707456246231d029a005a240322222633010400001a0a02020058030200595b", hexString1);
        assertEquals(hexString1, hexString2);
        assertEquals(hexString1, hexString3);
    }

    @Test
    void testDecode() {
        final String hexString = "0200402a01000000000139111122220063000000580000006f01dc9a0707456246231d029a005a240322222633010400001a0a02020058030200595b";

        final RustStyleDebugEntity02ForDecode decodedEntity1 = this.doDecode(RustStyleDebugEntity02ForDecode.class, hexString);
        doCompare(this.rustStyleSourceEntity, decodedEntity1);
        doCompareExtraItem(this.rustStyleSourceEntity.getExtraItems(), decodedEntity1.getExtraItems());

        final JtStyleDebugEntity02ForDecode decodedEntity2 = this.doDecode(JtStyleDebugEntity02ForDecode.class, hexString);
        doCompare(this.jtStyleSourceEntity, decodedEntity2);
        doCompareExtraItem(this.jtStyleSourceEntity.getExtraItems(), decodedEntity2.getExtraItems());

        final RawStyleDebugEntity02ForDecode decodedEntity3 = this.doDecode(RawStyleDebugEntity02ForDecode.class, hexString);
        doCompare(this.rawStyleSourceEntity, decodedEntity3);
        doCompareExtraItem(this.rawStyleSourceEntity.getExtraItems(), decodedEntity3.getExtraItems());
    }

    void doCompare(DebugEntity02ForJunitPurpose entity1, DebugEntity02ForJunitPurpose entity2) {
        assertEquals(entity1.getMsgId(), entity2.getMsgId());
        assertEquals(entity1.getMsgBodyProps(), entity2.getMsgBodyProps());
        assertEquals(entity1.getProtocolVersion(), entity2.getProtocolVersion());
        assertEquals(entity1.getTerminalId(), entity2.getTerminalId());
        assertEquals(entity1.getMsgSerialNo(), entity2.getMsgSerialNo());
        assertEquals(entity1.getSubPackageInfo(), entity2.getSubPackageInfo());

        assertEquals(entity1.getAlarmFlag(), entity2.getAlarmFlag());
        assertEquals(entity1.getStatus(), entity2.getStatus());
        assertEquals(entity1.getLatitude(), entity2.getLatitude());
        assertEquals(entity1.getLongitude(), entity2.getLongitude());
        assertEquals(entity1.getAltitude(), entity2.getAltitude());
        assertEquals(entity1.getSpeed(), entity2.getSpeed());
        assertEquals(entity1.getTime(), entity2.getTime());
    }

    void doCompareExtraItem(List<? extends DebugEntity02ForJunitPurpose.ExtraItemForJunitPurpose> list1, List<? extends DebugEntity02ForJunitPurpose.ExtraItemForJunitPurpose> list2) {
        assertEquals(list1.size(), list2.size());
        for (int i = 0; i < list1.size(); i++) {
            final DebugEntity02ForJunitPurpose.ExtraItemForJunitPurpose item1 = list1.get(i);
            final DebugEntity02ForJunitPurpose.ExtraItemForJunitPurpose item2 = list2.get(i);
            assertEquals(item1.getId(), item2.getId());
            assertEquals(item1.getContentLength(), item2.getContentLength());
            assertArrayEquals(item1.getContent(), item2.getContent());
        }
    }

    private void resetEntityBasicProperties(DebugEntity02ForJunitPurpose entity) {
        entity.setMsgId(0x0200);
        entity.setProtocolVersion((byte) 1);
        entity.setTerminalId("00000000013911112222");
        entity.setMsgSerialNo(99);
        entity.setSubPackageInfo(null);

        entity.setAlarmFlag(88);

        entity.setStatus(111);

        entity.setLatitude(31234567);

        entity.setLongitude(121987654);
        entity.setSpeed(666);
        entity.setAltitude(8989);
        entity.setDirection(90);
        entity.setTime("240322222633");

        entity.setCheckSum((byte) 91);

    }

    private void setMsgBodyProps(DebugEntity02ForJunitPurpose entity, List<? extends DebugEntity02ForJunitPurpose.ExtraItemForJunitPurpose> extraItemList) {

        // 消息体长度
        int msgBodyLength = 0;
        // 报警标志  DWORD(4)
        msgBodyLength += 4;
        // 状态  DWORD(4)
        msgBodyLength += 4;
        // 纬度  DWORD(4)
        msgBodyLength += 4;
        // 经度  DWORD(4)
        msgBodyLength += 4;
        // 高程  WORD(2)
        msgBodyLength += 2;
        // 速度  WORD(2)
        msgBodyLength += 2;
        // 方向  WORD(2)
        msgBodyLength += 2;
        // 时间  BCD[6] yyMMddHHmmss
        msgBodyLength += 6;

        // 附加项长度
        // +1: 附加信息ID 的长度
        // +1: 附加信息长度 的长度
        // +it.getContent().length: 附加消息内容 的长度
        msgBodyLength += (extraItemList.stream().mapToInt(it -> 1 + 1 + it.getContent().length).sum());

        final int props = generateMsgBodyPropsForJt808(msgBodyLength, 0, false, 0);
        entity.setMsgBodyProps(props);
    }
}
