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

package io.github.hylexus.xtream.codec.ext.jt1078.codec;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.*;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class Jt1078ByteToMessageDecoderTest {


    @Test
    void test() {
        final CompositeByteBuf compositeByteBuf = loadRawStreamData("mock-data/raw-stream/sim-length-6-h264-adpcma.txt");
        final Jt1078ByteToMessageDecoder decoder = new Jt1078ByteToMessageDecoder(Jt1078TerminalIdConverter.DEFAULT);
        final List<Object> packageInfoList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            decoder.decodePackage(compositeByteBuf, packageInfoList);
        }

        for (int i = 0; i < packageInfoList.size(); i++) {
            final Jt1078ByteToMessageDecoder.Jt1078PackageInfo packageInfo = (Jt1078ByteToMessageDecoder.Jt1078PackageInfo) packageInfoList.get(i);
            final Jt1078RequestHeader header = packageInfo.header();
            assertEquals(i, header.sequenceNumber());
            assertEquals(6, header.simLength());
            assertEquals("13800138999", header.convertedSim());
            assertEquals(2, header.channelNumber());
            assertTrue(header.msgBodyLength() <= 950);
        }

        compositeByteBuf.release();
        assertEquals(0, compositeByteBuf.refCnt());
    }

    @Test
    void test2() {
        final CompositeByteBuf compositeByteBuf = loadRawStreamData("mock-data/raw-stream/sim-length-10-h264-g711a.txt");
        final Jt1078ByteToMessageDecoder decoder = new Jt1078ByteToMessageDecoder(Jt1078TerminalIdConverter.DEFAULT);
        final List<Object> packageInfoList = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            decoder.decodePackage(compositeByteBuf, packageInfoList);
        }

        for (int i = 0; i < packageInfoList.size(); i++) {
            final Jt1078ByteToMessageDecoder.Jt1078PackageInfo packageInfo = (Jt1078ByteToMessageDecoder.Jt1078PackageInfo) packageInfoList.get(i);
            final Jt1078RequestHeader header = packageInfo.header();
            assertEquals(i, header.sequenceNumber());
            assertEquals(10, header.simLength());
            assertEquals("20232609559", header.convertedSim());
            assertEquals(1, header.channelNumber());
            assertTrue(header.msgBodyLength() <= 950);
        }

        compositeByteBuf.release();
        assertEquals(0, compositeByteBuf.refCnt());
    }

    @Test
    void test3() {
        final DefaultJt1078RequestHeader header = new DefaultJt1078RequestHeader();
        header.offset4(Jt1078RequestHeader.generateOffset4());
        header.m((byte) 1);
        header.payloadType(Jt1078PayloadType.H264);
        header.sequenceNumber(123);
        header.rawSim("013800138999");
        header.channelNumber((short) 2);
        header.dataType(Jt1078DataType.VIDEO_B);
        header.subPackageIdentifier(Jt1078SubPackageIdentifier.ATOMIC);
        header.timestamp(111111L);
        header.lastIFrameInterval(33);
        header.lastFrameInterval(44);
        header.msgBodyLength(5);

        final ByteBuf byteBuf = header.encode(true).writeBytes(new byte[]{1, 2, 3, 4, 5});
        final CompositeByteBuf byteBufList = ByteBufAllocator.DEFAULT.compositeBuffer()
                .addComponent(true, byteBuf.slice())
                .addComponent(true, byteBuf.slice());
        try {
            final Jt1078ByteToMessageDecoder decoder = new Jt1078ByteToMessageDecoder(Jt1078TerminalIdConverter.DEFAULT);
            final List<Object> packageInfoList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                decoder.decodePackage(byteBufList, packageInfoList);
            }
            assertEquals("3031636481e2007b0138001389990220000000000001b2070021002c00050102030405", FormatUtils.toHexString(byteBuf));
            assertEquals(1, packageInfoList.size());
            final Jt1078ByteToMessageDecoder.Jt1078PackageInfo packageInfo = (Jt1078ByteToMessageDecoder.Jt1078PackageInfo) packageInfoList.getFirst();
            assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, XtreamBytes.getBytes(packageInfo.body()));
            assertHeaderEquals(header, packageInfo.header());
        } finally {
            XtreamBytes.releaseBuf(byteBuf);
            assertEquals(0, byteBuf.refCnt());
        }
    }

    private void assertHeaderEquals(DefaultJt1078RequestHeader expected, Jt1078RequestHeader actual) {
        assertEquals(expected.offset4(), actual.offset4());
        assertEquals(expected.m(), actual.m());
        assertEquals(expected.payloadType(), actual.payloadType());
        assertEquals(expected.sequenceNumber(), actual.sequenceNumber());
        assertEquals(6, actual.simLength());
        assertEquals(expected.rawSim(), actual.rawSim());
        assertEquals("13800138999", actual.convertedSim());
        assertEquals(expected.channelNumber(), actual.channelNumber());
        assertEquals(expected.dataType(), actual.dataType());
        assertEquals(expected.subPackageIdentifier(), actual.subPackageIdentifier());
        assertEquals(expected.timestamp(), actual.timestamp());
        assertEquals(expected.lastIFrameInterval(), actual.lastIFrameInterval());
        assertEquals(expected.lastFrameInterval(), actual.lastFrameInterval());
        assertEquals(expected.msgBodyLength(), actual.msgBodyLength());
    }

    static CompositeByteBuf loadRawStreamData(String resourceName) {
        final CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        try (
                final InputStream inputStream = Jt1078ByteToMessageDecoderTest.class.getClassLoader().getResourceAsStream(resourceName);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("//") || line.startsWith("#")) {
                    continue;
                }
                buffer.addComponent(true, XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, line));
            }
            return buffer;
        } catch (Exception e) {
            XtreamBytes.releaseBuf(buffer);
            throw new RuntimeException(e);
        }
    }

}
