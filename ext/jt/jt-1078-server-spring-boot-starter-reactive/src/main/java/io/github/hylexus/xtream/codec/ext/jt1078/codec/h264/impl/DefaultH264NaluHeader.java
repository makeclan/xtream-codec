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

import io.github.hylexus.xtream.codec.ext.jt1078.codec.h264.H264NaluHeader;
import lombok.Getter;

@Getter
public class DefaultH264NaluHeader implements H264NaluHeader {

    private final byte forbiddenBit;

    private final byte nalRefIdc;

    private final byte typeValue;

    public DefaultH264NaluHeader(int value) {
        this.forbiddenBit = (byte) (value >>> 7);
        this.nalRefIdc = (byte) ((value & 0b01100000) >> 5);
        this.typeValue = (byte) (value & 0b11111);
    }

    @Override
    public byte forbiddenBit() {
        return this.forbiddenBit;
    }

    @Override
    public byte nalRefIdc() {
        return this.nalRefIdc;
    }

    @Override
    public byte typeValue() {
        return this.typeValue;
    }

    @Override
    public String toString() {
        return "NaluHeader{"
                + "F=" + forbiddenBit()
                + ", NRI=" + nir()
                + ", TYPE=" + type().orElse(null)
                + '}';
    }
}
