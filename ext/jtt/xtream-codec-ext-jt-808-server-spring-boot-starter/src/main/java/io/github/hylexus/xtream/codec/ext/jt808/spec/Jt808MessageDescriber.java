/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.ext.jt808.spec;

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
