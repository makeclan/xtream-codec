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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request;

import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.BaseCodecTest;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.AlarmIdentifier;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.LocationItem0x64;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.LocationItem0x12;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.LocationItem0x13;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMessage0200Sample2Test extends BaseCodecTest {
    final LocalDateTime time = LocalDateTime.of(2014, 10, 21, 19, 51, 9);

    @Test
    void testEncode() {
        final BuiltinMessage0200Sample2 entity = new BuiltinMessage0200Sample2();
        entity.setAlarmFlag(123L);
        entity.setStatus(222);
        entity.setLatitude(31000562);
        entity.setLongitude(121451372);
        entity.setAltitude(666);
        entity.setSpeed(78);
        entity.setDirection(0);
        entity.setTime(time);

        final Map<Short, Object> extraItems = new LinkedHashMap<>();
        extraItems.put((short) 0x01, 111L);
        extraItems.put((short) 0x02, 222);
        extraItems.put((short) 0x03, 666);
        extraItems.put((short) 0x04, 333);
        extraItems.put((short) 0x25, 777L);
        extraItems.put((short) 0x30, (short) 33);
        extraItems.put((short) 0x12, new LocationItem0x12().setLocationType((short) 1).setAreaId(0).setDirection((short) 1));
        extraItems.put((short) 0x13, new LocationItem0x13().setLineId(1L).setLineDrivenTime(0).setResult((short) 0));
        extraItems.put((short) 0x31, (short) 55);
        extraItems.put((short) 0x64, new LocationItem0x64()
                .setAlarmId(222L)
                .setStatus((short) 223)
                .setAlarmType((short) 111)
                .setAlarmLevel((short) 3)
                .setSpeedOfFrontObject((short) 66)
                .setDistanceToFrontObject((short) 11)
                .setDeviationType((short) 12)
                .setRoadSignType((short) 13)
                .setRoadSignData((short) 14)
                .setSpeed((short) 15)
                .setHeight(1234)
                .setLatitude(31000562L)
                .setLongitude(121451372L)
                .setDatetime(time)
                .setVehicleStatus(1)
                .setAlarmIdentifier(new AlarmIdentifier()
                        .setTerminalId("1234567")
                        .setTime(time)
                        .setSequence((short) 1)
                        .setAttachmentCount((short) 1)
                        .setReserved((short) 0)
                )
        );
        entity.setExtraItems(extraItems);

        final String hex = encode(entity, Jt808ProtocolVersion.VERSION_2019, terminalId2019, 0x0200);
        assertEquals("7e0200407c010000000001391234432900000000007b000000de01d907f2073d336c029a004e000014102119510901040000006f020200de0302029a0402014d2504000003093001211206010000000001130700000001000000310137642f000000dedf6f03420b0c0d0e0f04d201d907f2073d336c1410211951090001313233343536371410211951090101008e7e", hex);
    }

    @Test
    void testDecode() {
        final BuiltinMessage0200Sample2 entity = decodeAsEntity(BuiltinMessage0200Sample2.class, "0200407c010000000001391234432900000000007b000000de01d907f2073d336c029a004e000014102119510901040000006f020200de0302029a0402014d2504000003093001211206010000000001130700000001000000310137642f000000dedf6f03420b0c0d0e0f04d201d907f2073d336c1410211951090001313233343536371410211951090101008e");

        assertEquals(123L, entity.getAlarmFlag());
        assertEquals(222, entity.getStatus());
        assertEquals(31000562, entity.getLatitude());
        assertEquals(121451372, entity.getLongitude());
        assertEquals(666, entity.getAltitude());
        assertEquals(78, entity.getSpeed());
        assertEquals(0, entity.getDirection());
        assertEquals(time, entity.getTime());

        final Map<Short, Object> extraItems = entity.getExtraItems();
        assertEquals(111L, extraItems.get((short) 0x01));
        assertEquals(222, extraItems.get((short) 0x02));
        assertEquals(666, extraItems.get((short) 0x03));
        assertEquals(333, extraItems.get((short) 0x04));
        assertEquals(777L, extraItems.get((short) 0x25));
        assertEquals((short) 33, extraItems.get((short) 0x30));
        assertEquals((short) 55, extraItems.get((short) 0x31));

        final LocationItem0x64 item64 = (LocationItem0x64) extraItems.get((short) 0x64);
        assertEquals(222L, item64.getAlarmId());
        assertEquals((short) 223, item64.getStatus());
        assertEquals((short) 111, item64.getAlarmType());
        assertEquals((short) 3, item64.getAlarmLevel());
        assertEquals((short) 66, item64.getSpeedOfFrontObject());
        assertEquals((short) 11, item64.getDistanceToFrontObject());
        assertEquals((short) 12, item64.getDeviationType());
        assertEquals((short) 13, item64.getRoadSignType());
        assertEquals((short) 14, item64.getRoadSignData());
        assertEquals((short) 15, item64.getSpeed());
        assertEquals(1234, item64.getHeight());
        assertEquals(31000562L, item64.getLatitude());
        assertEquals(121451372L, item64.getLongitude());
        assertEquals(time, item64.getDatetime());
        assertEquals(1, item64.getVehicleStatus());
        assertEquals("1234567", item64.getAlarmIdentifier().getTerminalId());
        assertEquals(time, item64.getAlarmIdentifier().getTime());
        assertEquals((short) 1, item64.getAlarmIdentifier().getSequence());
        assertEquals((short) 1, item64.getAlarmIdentifier().getAttachmentCount());
        assertEquals((short) 0, item64.getAlarmIdentifier().getReserved());
    }
}
