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

import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264Nalu;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264NaluHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078DataType;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

public class DefaultH264Nalu implements H264Nalu {

    private final H264NaluHeader header;
    private final ByteBuf data;
    private final Jt1078DataType dataType;
    private final Integer lastIFrameInterval;
    private final Integer lastFrameInterval;
    private final Long timestamp;

    public DefaultH264Nalu(H264NaluHeader header, ByteBuf data, Jt1078DataType dataType, Integer lastIFrameInterval, Integer lastFrameInterval, Long timestamp) {
        this.header = header;
        this.data = data;
        this.dataType = dataType;
        this.lastIFrameInterval = lastIFrameInterval;
        this.lastFrameInterval = lastFrameInterval;
        this.timestamp = timestamp;
    }

    @Override
    public H264NaluHeader header() {
        return this.header;
    }

    @Override
    public ByteBuf data() {
        return this.data;
    }

    @Override
    public Jt1078DataType dataType() {
        return this.dataType;
    }

    @Override
    public Optional<Integer> lastIFrameInterval() {
        return Optional.ofNullable(this.lastIFrameInterval);
    }

    @Override
    public Optional<Integer> lastFrameInterval() {
        return Optional.ofNullable(this.lastFrameInterval);
    }

    @Override
    public Optional<Long> timestamp() {
        return Optional.ofNullable(this.timestamp);
    }

    @Override
    public String toString() {
        return "DefaultH264Nalu{"
                + "header=" + header
                + ", rbsp=" + data
                + ", dataType=" + dataType
                + ", lastIFrameInterval=" + lastIFrameInterval
                + ", lastFrameInterval=" + lastFrameInterval
                + ", timestamp=" + timestamp
                + '}';
    }
}
