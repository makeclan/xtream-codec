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

package io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.impl;


import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.tag.AudioFlvTag;

import java.util.Objects;
import java.util.Optional;

public class DefaultAudioFlvFlvTagHeader implements AudioFlvTag.AudioFlvTagHeader, AudioFlvTag.AudioFlvTagHeaderBuilder {
    private AudioFlvTag.AudioSoundFormat soundFormat;
    private AudioFlvTag.AudioSoundRate soundRate;
    private AudioFlvTag.AudioSoundSize soundSize;
    private AudioFlvTag.AudioSoundType soundType;
    private AudioFlvTag.AudioAacPacketType aacPacketType;
    private byte[] aacSequenceHeader;

    public DefaultAudioFlvFlvTagHeader() {
    }

    public DefaultAudioFlvFlvTagHeader(
            AudioFlvTag.AudioSoundFormat soundFormat, AudioFlvTag.AudioSoundRate soundRate,
            AudioFlvTag.AudioSoundSize soundSize, AudioFlvTag.AudioSoundType soundType,
            AudioFlvTag.AudioAacPacketType aacPacketType, byte[] aacSequenceHeader) {
        this.soundFormat = soundFormat;
        this.soundRate = soundRate;
        this.soundSize = soundSize;
        this.soundType = soundType;
        this.aacPacketType = aacPacketType;
        this.aacSequenceHeader = aacSequenceHeader;
    }

    @Override
    public AudioFlvTag.AudioSoundFormat soundFormat() {
        return this.soundFormat;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundFormat(AudioFlvTag.AudioSoundFormat soundFormat) {
        this.soundFormat = soundFormat;
        return this;
    }

    @Override
    public AudioFlvTag.AudioSoundRate soundRate() {
        return this.soundRate;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundRate(AudioFlvTag.AudioSoundRate soundRate) {
        this.soundRate = soundRate;
        return this;
    }

    @Override
    public AudioFlvTag.AudioSoundSize soundSize() {
        return this.soundSize;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundSize(AudioFlvTag.AudioSoundSize soundSize) {
        this.soundSize = soundSize;
        return this;
    }

    @Override
    public AudioFlvTag.AudioSoundType soundType() {
        return this.soundType;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundType(AudioFlvTag.AudioSoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    @Override
    public AudioFlvTag.AudioAacPacketType aacPacketType() {
        return this.aacPacketType;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder aacPacketType(AudioFlvTag.AudioAacPacketType aacPacketType) {
        this.aacPacketType = aacPacketType;
        return this;
    }

    @Override
    public byte[] aacSequenceHeader() {
        return this.aacSequenceHeader;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder aacSequenceHeader(byte[] aacSequenceHeader) {
        this.aacSequenceHeader = aacSequenceHeader;
        return this;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeader build() {
        return new DefaultAudioFlvFlvTagHeader(
                Objects.requireNonNull(this.soundFormat, "soundFormat is null"),
                Objects.requireNonNull(this.soundRate, "soundFormat is null"),
                Objects.requireNonNull(this.soundSize, "soundSize is null"),
                Objects.requireNonNull(this.soundType, "soundType is null"),
                this.aacPacketType,
                this.aacSequenceHeader
        );
    }
}
