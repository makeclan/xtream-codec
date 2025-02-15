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

package io.github.hylexus.xtream.codec.ext.jt808.spec;

import io.github.hylexus.xtream.codec.core.tracker.CodecTracker;

/**
 * @author hylexus
 */
public class Jt808MessageDescriber {
    protected Jt808ProtocolVersion version;
    protected String terminalId;
    protected Integer messageId;
    protected int maxPackageSize = 1024;
    protected byte reversedBit15InHeader = 0;
    protected byte encryptionType = 0b000;
    protected int flowId = -1;
    protected Jt808FlowIdGenerator flowIdGenerator;
    protected CodecTracker bodyCodecTracker;
    protected Jt808MessageCodecTracker messageCodecTracker = Jt808MessageCodecTracker.NO_OP;

    public Jt808MessageDescriber(Jt808ProtocolVersion version, String terminalId) {
        this.version = version;
        this.terminalId = terminalId;
    }

    public Jt808ProtocolVersion version() {
        return version;
    }

    public Jt808MessageDescriber version(Jt808ProtocolVersion version) {
        this.version = version;
        return this;
    }

    public String terminalId() {
        return terminalId;
    }

    public Jt808MessageDescriber terminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public Integer messageId() {
        return messageId;
    }

    public Jt808MessageDescriber messageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public int maxPackageSize() {
        return maxPackageSize;
    }

    public Jt808MessageDescriber maxPackageSize(int maxPackageSize) {
        this.maxPackageSize = maxPackageSize;
        return this;
    }

    public byte reversedBit15InHeader() {
        return reversedBit15InHeader;
    }

    public Jt808MessageDescriber reversedBit15InHeader(byte reversedBit15InHeader) {
        this.reversedBit15InHeader = reversedBit15InHeader;
        return this;
    }

    public byte encryptionType() {
        return encryptionType;
    }

    public Jt808MessageDescriber encryptionType(byte encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public int flowId() {
        return flowId;
    }

    public Jt808MessageDescriber flowId(int flowId) {
        this.flowId = flowId;
        return this;
    }

    public CodecTracker bodyCodecTracker() {
        return this.bodyCodecTracker;
    }

    public Jt808MessageDescriber bodyCodecTracker(CodecTracker tracker) {
        this.bodyCodecTracker = tracker;
        return this;
    }

    public Jt808MessageCodecTracker messageCodecTracker() {
        return this.messageCodecTracker;
    }

    public Jt808MessageDescriber messageCodecTracker(Jt808MessageCodecTracker messageCodecTracker) {
        this.messageCodecTracker = messageCodecTracker;
        return this;
    }

    public Jt808FlowIdGenerator flowIdGenerator() {
        return this.flowIdGenerator;
    }

    public Jt808MessageDescriber flowIdGenerator(Jt808FlowIdGenerator flowIdGenerator) {
        this.flowIdGenerator = flowIdGenerator;
        return this;
    }

    public Jt808MessageDescriber check() {
        if (this.version() == null) {
            throw new IllegalArgumentException("version() is null");
        }
        if (this.terminalId() == null) {
            throw new IllegalArgumentException("terminalId() is null");
        }
        if (this.messageId() == null) {
            throw new IllegalArgumentException("messageId() is null");
        }
        if (this.maxPackageSize() <= 0) {
            throw new IllegalArgumentException("maxPackageSize() is invalid");
        }
        return this;
    }
}
