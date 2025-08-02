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

package io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl;

import io.github.hylexus.xtream.codec.core.utils.ByteRingBuffer;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078SubscriberCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.StringJoiner;

@Getter
@Setter
@SuperBuilder
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class H264Jt1078SubscriberCreator extends Jt1078SubscriberCreator {

    @Builder.Default
    private H264Meta h264Meta = new H264Meta(1 << 18);

    /**
     * @param naluDecoderRingBufferSize 必须是 2 的 N 次幂。详情见 {@link io.github.hylexus.xtream.codec.core.utils.ByteRingBuffer}
     * @see io.github.hylexus.xtream.codec.core.utils.ByteRingBuffer
     */
    public record H264Meta(int naluDecoderRingBufferSize) {

        @SuppressWarnings("redundent")
        public H264Meta(int naluDecoderRingBufferSize) {
            if (ByteRingBuffer.isInvalidCapacity(naluDecoderRingBufferSize)) {
                throw new IllegalArgumentException("naluDecoderRingBufferSize must be a power of 2");
            }
            this.naluDecoderRingBufferSize = naluDecoderRingBufferSize;
        }

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", H264Jt1078SubscriberCreator.class.getSimpleName() + "[", "]")
                .add("convertedSim='" + convertedSim + "'")
                .add("rawSim='" + rawSim + "'")
                .add("channelNumber=" + channelNumber)
                .add("hasAudio=" + hasAudio)
                .add("hasVideo=" + hasVideo)
                .add("h264Meta=" + h264Meta)
                .add("timeout=" + timeout)
                .add("desc='" + desc + "'")
                .add("metadata=" + metadata)
                .toString();
    }

}
