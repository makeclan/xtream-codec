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

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TerminalIdConverter;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            assertEquals("13800138999", header.sim());
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
            assertEquals("20232609559", header.sim());
            assertEquals(1, header.channelNumber());
            assertTrue(header.msgBodyLength() <= 950);
        }

        compositeByteBuf.release();
        assertEquals(0, compositeByteBuf.refCnt());
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
