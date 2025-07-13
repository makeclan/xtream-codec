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

package io.github.hylexus.xtream.codec.ext.jt1078.codec.flv;

import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;

import java.util.List;

public interface FlvEncoder {

    default FlvHeader createFlvHeader(boolean hasVideo, boolean hasAudio) {
        return FlvHeader.of(hasVideo, hasAudio);
    }

    List<ByteBuf> encodeVideoTag(long timestamp, ByteBuf naluStream);

    ByteBuf encodeAudioTag(int dts, AudioFlvTag.AudioFlvTagHeader audioTagHeader, ByteBuf payload);

    /**
     * @return ScriptTag + SPS  + PPS
     */
    ByteBuf getFlvBasicFrame();

    /**
     * @return 最近一个关键帧
     */
    ByteBuf getLastIFrame();

    default void close() {
    }

}
