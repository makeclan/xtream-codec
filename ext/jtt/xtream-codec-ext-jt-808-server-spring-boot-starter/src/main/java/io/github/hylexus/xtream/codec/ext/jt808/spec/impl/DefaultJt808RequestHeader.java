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

package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Accessors(fluent = true, chain = true)
@Setter
public class DefaultJt808RequestHeader implements Jt808RequestHeader {

    private Jt808ProtocolVersion version;

    // byte[0-1] 消息ID
    private int messageId;

    // byte[2-3] 消息体属性
    private Jt808MessageBodyProps messageBodyProps;

    // byte[4-9] 终端Id
    private String terminalId;

    // byte[10-11] 流水号
    private int flowId;

    private Jt808SubPackageProps subPackageProps;

    public DefaultJt808RequestHeader() {
    }

    public DefaultJt808RequestHeader(
            Jt808ProtocolVersion version, int messageId, Jt808MessageBodyProps messageBodyProps, String terminalId, int flowId, Jt808SubPackageProps subPackageProps) {
        this.version = version;
        this.messageId = messageId;
        this.messageBodyProps = messageBodyProps;
        this.terminalId = terminalId;
        this.flowId = flowId;
    }

    @Override
    public Jt808ProtocolVersion version() {
        return version;
    }

    @Override
    public int messageId() {
        return messageId;
    }

    @Override
    public Jt808MessageBodyProps messageBodyProps() {
        return messageBodyProps;
    }

    @Override
    public String terminalId() {
        return terminalId;
    }

    @Override
    public int flowId() {
        return flowId;
    }

    @Override
    public Jt808SubPackageProps subPackage() {
        return this.subPackageProps;
    }

    public void setMessageBodyPropsField(int messageBodyPropsField) {
        this.messageBodyProps = new DefaultJt808MessageBodyProps(messageBodyPropsField);
    }

    @Override
    public String toString() {
        return "HeaderSpec{"
                + "version=" + version
                + ", terminalId='" + terminalId + '\''
                + ", messageId=" + messageId
                + ", flowId=" + flowId
                + ", messageBodyProps=" + messageBodyProps
                + '}';
    }

    public static class DefaultJt808MessageHeaderBuilder implements Jt808MessageHeaderBuilder {

        private Jt808ProtocolVersion version;
        private int messageId;
        private Jt808MessageBodyProps messageBodyProps;
        private String terminalId;
        private int flowId;
        private Jt808SubPackageProps subPackageProps;

        public DefaultJt808MessageHeaderBuilder() {
        }

        public DefaultJt808MessageHeaderBuilder(Jt808RequestHeader header) {
            this.messageId = header.messageId();
            this.messageBodyProps = header.messageBodyProps();
            this.terminalId = header.terminalId();
            this.flowId = header.flowId();
            this.version = header.version();
        }

        @Override
        public Jt808MessageHeaderBuilder version(Jt808ProtocolVersion version) {
            this.version = version;
            return this;
        }

        @Override
        public Jt808MessageHeaderBuilder messageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public Jt808MessageHeaderBuilder messageBodyProps(Jt808MessageBodyProps messageBodyProps) {
            this.messageBodyProps = messageBodyProps;
            return this;
        }

        @Override
        public Jt808MessageHeaderBuilder terminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        @Override
        public Jt808MessageHeaderBuilder flowId(int flowId) {
            this.flowId = flowId;
            return this;
        }

        @Override
        public Jt808MessageHeaderBuilder subPackageProps(Jt808SubPackageProps subPackageProps) {
            this.subPackageProps = subPackageProps;
            return this;
        }

        @Override
        public Jt808RequestHeader build() {
            return new DefaultJt808RequestHeader(version, messageId, messageBodyProps, terminalId, flowId, subPackageProps);
        }
    }

}
