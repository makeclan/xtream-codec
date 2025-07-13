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


import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264Decoder;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264Nalu;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264NaluHeader;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * StartCodePrefix 可能是 001 也可能是 0001
 * <p>
 * 该实现中的状态机算法来自 <a href="https://github.com/samirkumardas/jmuxer/blob/579835d7d72796d3374e0c62929456e7202bcb12/src/parsers/h264.js#LL7C18-L7C18">github--jmuxer--h264.js</a>
 *
 * @see <a href="https://www.bilibili.com/video/BV1b3411X7eE">https://www.bilibili.com/video/BV1b3411X7eE</a>
 * @see <a href="https://github.com/samirkumardas/jmuxer/blob/579835d7d72796d3374e0c62929456e7202bcb12/src/parsers/h264.js#LL7C18-L7C18">github--jmuxer--h264.js</a>
 * @see <a href="https://gitee.com/ldming/JT1078">https://gitee.com/ldming/JT1078</a>
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 */
public class DefaultH264DecoderBackup implements H264Decoder {

    public DefaultH264DecoderBackup() {
    }

    @Override
    public List<H264Nalu> decode(ByteBuf byteBuf) {
        final List<H264Nalu> result = new ArrayList<>();
        final int length = byteBuf.readableBytes();

        int i = 0;
        int state = 0;
        int sliceStartIndex = -1;

        while (i < length) {
            final byte value = byteBuf.getByte(i++);
            switch (state) {
                case 0: {
                    if (value == 0) {
                        state = 1;
                    }
                    break;
                }
                // [0]01,[0]001
                case 1: {
                    state = value == 0 ? 2 : 0;
                    break;
                }
                // [00]1,[00]01
                case 2: {
                    if (value == 1 && i < length) {
                        // match [001]
                        final ByteBuf data = byteBuf.slice(sliceStartIndex, i - state - sliceStartIndex - 1);
                        final H264Nalu nalu = this.createH264Nalu(data);
                        result.add(nalu);
                        sliceStartIndex = i;
                        state = 0;
                    } else if (value == 0) {
                        state = 3;
                    } else {
                        state = 0;
                    }
                    break;
                }
                // [000]1
                case 3: {
                    // match [0001]
                    if (value == 1 && i < length) {
                        if (sliceStartIndex >= 0) {
                            final ByteBuf data = byteBuf.slice(sliceStartIndex, i - state - sliceStartIndex - 1);
                            final H264Nalu nalu = this.createH264Nalu(data);
                            result.add(nalu);
                        }
                        sliceStartIndex = i;
                    }
                    state = 0;
                    break;
                }
                default: {
                    break;
                }
            }
        }

        if (sliceStartIndex >= 0) {
            final ByteBuf data = byteBuf.slice(sliceStartIndex, length - sliceStartIndex);
            result.add(this.createH264Nalu(data));
        }

        return result;
    }


    protected H264Nalu createH264Nalu(ByteBuf data) {
        final H264NaluHeader h264NaluHeader = H264NaluHeader.of(data.getByte(0));
        return new DefaultH264Nalu(
                h264NaluHeader,
                // data.slice(1, data.readableBytes() - 1),
                data.slice(0, data.readableBytes())
        );
    }
}
