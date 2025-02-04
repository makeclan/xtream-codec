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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.bo;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;

public class EncodedMessageMetadata {
    private int totalPackageNo;
    private int currentPackageNo;
    private String rawHexString;
    private String escapedHexString;
    private Jt808RequestHeader header;
    private int flowId;
    private String bodyBeforeEncryption;
    private byte checkSum;

    public EncodedMessageMetadata() {
    }

    public byte getCheckSum() {
        return checkSum;
    }

    public EncodedMessageMetadata setCheckSum(byte checkSum) {
        this.checkSum = checkSum;
        return this;
    }

    public int getTotalPackageNo() {
        return totalPackageNo;
    }

    public EncodedMessageMetadata setTotalPackageNo(int totalPackageNo) {
        this.totalPackageNo = totalPackageNo;
        return this;
    }

    public int getCurrentPackageNo() {
        return currentPackageNo;
    }

    public EncodedMessageMetadata setCurrentPackageNo(int currentPackageNo) {
        this.currentPackageNo = currentPackageNo;
        return this;
    }

    public String getRawHexString() {
        return rawHexString;
    }

    public EncodedMessageMetadata setRawHexString(String rawHexString) {
        this.rawHexString = rawHexString;
        return this;
    }

    public String getEscapedHexString() {
        return escapedHexString;
    }

    public EncodedMessageMetadata setEscapedHexString(String escapedHexString) {
        this.escapedHexString = escapedHexString;
        return this;
    }

    public Jt808RequestHeader getHeader() {
        return header;
    }

    public EncodedMessageMetadata setHeader(Jt808RequestHeader header) {
        this.header = header;
        return this;
    }

    public int getFlowId() {
        return flowId;
    }

    public EncodedMessageMetadata setFlowId(int flowId) {
        this.flowId = flowId;
        return this;
    }

    public String getBodyBeforeEncryption() {
        return bodyBeforeEncryption;
    }

    public EncodedMessageMetadata setBodyBeforeEncryption(String bodyBeforeEncryption) {
        this.bodyBeforeEncryption = bodyBeforeEncryption;
        return this;
    }
}
