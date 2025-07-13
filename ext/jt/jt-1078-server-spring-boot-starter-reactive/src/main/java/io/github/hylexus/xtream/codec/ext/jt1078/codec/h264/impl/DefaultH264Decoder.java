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

package io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.impl;

import io.github.hylexus.xtream.codec.core.utils.ByteRingBuffer;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264Decoder;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264Nalu;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264NaluHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

public class DefaultH264Decoder implements H264Decoder {

    private final ByteRingBuffer ringBuffer;

    // naluStart 是绝对索引，指向当前 NALU 数据开始的位置（不含StartCode）
    private int naluStart = -1;

    public DefaultH264Decoder(int capacity) {
        this.ringBuffer = new ByteRingBuffer(capacity);
    }

    @Override
    public List<H264Nalu> decode(ByteBuf input) {
        this.ringBuffer.writeBytes(input);

        int readerIndex = this.ringBuffer.readerIndex();
        int writerIndex = this.ringBuffer.writerIndex();

        int zeroCount = 0;
        int i = readerIndex;

        final List<H264Nalu> nalus = new ArrayList<>();
        while (i < writerIndex) {
            final byte b = ringBuffer.getByte(i);

            if (b == 0) {
                zeroCount++;
                i++;
            } else if (b == 1 && zeroCount >= 2) {
                final int startCodeLength = (zeroCount >= 3) ? 4 : 3;
                final int startCodePosition = i - zeroCount;

                if (naluStart == -1) {
                    // 第一次找到 StartCode，下一字节是NALU起点
                    naluStart = startCodePosition + startCodeLength;
                } else {
                    // 切割前一个NALU
                    final int naluLen = startCodePosition - naluStart;
                    if (naluLen > 0) {
                        final byte[] naluBytes = ringBuffer.peek(naluStart - readerIndex, naluLen);
                        final H264Nalu h264Nalu = createH264Nalu(Unpooled.wrappedBuffer(naluBytes));
                        nalus.add(h264Nalu);
                        ringBuffer.discard(naluStart - readerIndex + naluLen);
                        // long discarded =  naluStart - readerIndex +  naluLen;
                        readerIndex = ringBuffer.readerIndex();
                        writerIndex = ringBuffer.writerIndex();
                        i = readerIndex;
                        naluStart = startCodePosition + startCodeLength;
                        zeroCount = 0;
                        continue;
                    } else {
                        naluStart = startCodePosition + startCodeLength;
                    }
                }
                zeroCount = 0;
                i++;
            } else {
                zeroCount = 0;
                i++;
            }
        }

        return nalus;
    }

    protected H264Nalu createH264Nalu(ByteBuf data) {
        final H264NaluHeader h264NaluHeader = H264NaluHeader.of(data.getByte(0));
        return new DefaultH264Nalu(
                h264NaluHeader,
                // data.slice(1, data.readableBytes() - 1),
                data.slice(0, data.readableBytes())
        );
    }

    // 读取 ringBuffer 的最后一部分未闭合的数据
    // 直播的场景下 好像最后一部分数据可有可无 不用考虑
    public List<H264Nalu> flush() {
        final List<H264Nalu> nalus = new ArrayList<>();
        final int size = ringBuffer.readableBytes();
        if (naluStart != -1 && size > (naluStart - ringBuffer.readerIndex())) {
            final int offset = naluStart - ringBuffer.readerIndex();
            final int naluLength = size - offset;
            final byte[] naluBytes = ringBuffer.peek(offset, naluLength);
            nalus.add(createH264Nalu(Unpooled.wrappedBuffer(naluBytes)));
            ringBuffer.discard(size);
        }
        naluStart = -1;
        return nalus;
    }
}
