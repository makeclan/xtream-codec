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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class EncodeMessageDto {

    @NotNull(message = "messageId is null")
    private Integer messageId;
    @NotNull(message = "flowId is null")
    @Min(value = 0, message = "flowId > 0")
    private Integer flowId = 0;

    private byte reversedBit15InHeader = 0;

    @Min(value = 25, message = "maxPackageSize > 25")
    private int maxPackageSize = 1024;

    @NotNull(message = "bodyClass is null")
    private String bodyClass;
    private byte encryptionType = 0b000;

    @NotNull(message = "bodyData is null")
    private Map<String, Object> bodyData;

    public byte getEncryptionType() {
        return encryptionType;
    }

    public EncodeMessageDto setEncryptionType(byte encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public byte getReversedBit15InHeader() {
        return reversedBit15InHeader;
    }

    public EncodeMessageDto setReversedBit15InHeader(byte reversedBit15InHeader) {
        this.reversedBit15InHeader = reversedBit15InHeader;
        return this;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public EncodeMessageDto setMessageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getBodyClass() {
        return bodyClass;
    }

    public EncodeMessageDto setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
        return this;
    }

    public int getFlowId() {
        return flowId;
    }

    public EncodeMessageDto setFlowId(Integer flowId) {
        this.flowId = flowId;
        return this;
    }

    public Map<String, Object> getBodyData() {
        return bodyData;
    }

    public EncodeMessageDto setBodyData(Map<String, Object> bodyData) {
        this.bodyData = bodyData;
        return this;
    }

    public int getMaxPackageSize() {
        return maxPackageSize;
    }

    public EncodeMessageDto setMaxPackageSize(int maxPackageSize) {
        this.maxPackageSize = maxPackageSize;
        return this;
    }
}
