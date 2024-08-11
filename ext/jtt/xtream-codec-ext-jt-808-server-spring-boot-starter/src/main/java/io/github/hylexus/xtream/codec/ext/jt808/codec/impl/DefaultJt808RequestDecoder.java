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

package io.github.hylexus.xtream.codec.ext.jt808.codec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.exception.Jt808MessageDecodeException;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808MessageBodyProps;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808SubPackageProps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.NettyInbound;

public class DefaultJt808RequestDecoder implements Jt808RequestDecoder {

    private static final Logger log = LoggerFactory.getLogger(DefaultJt808RequestDecoder.class);
    protected final Jt808BytesProcessor jt808MessageProcessor;

    public DefaultJt808RequestDecoder(Jt808BytesProcessor jt808MessageProcessor) {
        this.jt808MessageProcessor = jt808MessageProcessor;
    }

    @Override
    public Jt808Request decode(ByteBufAllocator allocator, NettyInbound nettyInbound, ByteBuf payload) {
        if (log.isDebugEnabled()) {
            log.debug("- >>>>>>>>>>>>>>> : 7E{}7E", FormatUtils.toHexString(payload));
        }
        final ByteBuf escaped = this.jt808MessageProcessor.doEscapeForReceive(payload);

        if (log.isDebugEnabled()) {
            log.debug("+ >>>>>>>>>>>>>>> : 7E{}7E", FormatUtils.toHexString(escaped));
        }

        final Jt808RequestHeader header = this.parseMessageHeaderSpec(escaped);
        final int messageBodyStartIndex = Jt808RequestHeader.messageBodyStartIndex(header.version(), header.messageBodyProps().hasSubPackage());
        final byte originalCheckSum = escaped.getByte(escaped.readableBytes() - 1);
        final byte calculatedCheckSum = this.jt808MessageProcessor.calculateCheckSum(escaped.slice(0, escaped.readableBytes() - 1));
        final ByteBuf body = escaped.slice(messageBodyStartIndex, header.messageBodyLength());
        return new DefaultJt808Request(
                allocator,
                nettyInbound,
                payload,
                header,
                body,
                originalCheckSum,
                calculatedCheckSum
        );
    }

    protected Jt808RequestHeader parseMessageHeaderSpec(ByteBuf byteBuf) {
        // 1. bytes[0-1] WORD
        final int messageId = XtreamBytes.getWord(byteBuf, 0);
        // bytes[2-3] WORD 消息体属性
        final int messageBodyPropsIntValue = XtreamBytes.getWord(byteBuf, 2);
        final Jt808RequestHeader.Jt808MessageBodyProps messageBodyProps = new DefaultJt808MessageBodyProps(messageBodyPropsIntValue);

        final Jt808ProtocolVersion version = this.detectVersion(messageId, messageBodyProps, byteBuf);
        if (version.getVersionBit() == 1) {
            return this.parseHeaderGreatThanOrEqualsV2019(version, messageBodyProps, byteBuf);
        } else if (version == Jt808ProtocolVersion.VERSION_2013 || version == Jt808ProtocolVersion.VERSION_2011) {
            return this.parseHeaderLessThanOrEqualsV2013(version, messageBodyProps, byteBuf);
        }

        throw new Jt808MessageDecodeException("未知版本: " + version);
    }

    protected Jt808ProtocolVersion detectVersion(int messageId, Jt808RequestHeader.Jt808MessageBodyProps messageBodyProps, ByteBuf byteBuf) {
        if (messageBodyProps.versionIdentifier() != 1) {
            return Jt808ProtocolVersion.VERSION_2013;
        }

        final byte version = byteBuf.getByte(4);
        if (version == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return Jt808ProtocolVersion.VERSION_2019;
        }
        return Jt808ProtocolVersion.VERSION_2013;
    }

    protected Jt808RequestHeader parseHeaderGreatThanOrEqualsV2019(
            Jt808ProtocolVersion version, Jt808RequestHeader.Jt808MessageBodyProps messageBodyProps, ByteBuf byteBuf) {

        final DefaultJt808RequestHeader headerSpec = new DefaultJt808RequestHeader();
        headerSpec.version(version);
        // 1. bytes[0-1] WORD
        final int messageId = XtreamBytes.getWord(byteBuf, 0);
        headerSpec.messageId(messageId);
        // 2. bytes[2-3] WORD
        headerSpec.messageBodyProps(messageBodyProps);

        // 3. bytes[4] WORD 协议版本号
        // final byte version = byteBuf.getByte(4);
        // assert version == 1; // V2019
        // 4. byte[5-14]   终端手机号或设备ID bcd[10]
        headerSpec.terminalId(XtreamBytes.getBcd(byteBuf, 5, 10));

        // 4. byte[15-16]     消息流水号
        headerSpec.flowId(XtreamBytes.getWord(byteBuf, 15));
        if (messageBodyProps.hasSubPackage()) {
            final DefaultJt808SubPackageProps subPackageProps = new DefaultJt808SubPackageProps();
            subPackageProps.totalSubPackageCount(XtreamBytes.getWord(byteBuf, 17));
            subPackageProps.currentPackageNo(XtreamBytes.getWord(byteBuf, 19));
            headerSpec.subPackageProps(subPackageProps);
        }
        return headerSpec;
    }

    protected Jt808RequestHeader parseHeaderLessThanOrEqualsV2013(
            Jt808ProtocolVersion version, Jt808RequestHeader.Jt808MessageBodyProps messageBodyProps, ByteBuf byteBuf) {

        final DefaultJt808RequestHeader headerSpec = new DefaultJt808RequestHeader();
        headerSpec.version(version);
        // 1. bytes[0-1] WORD
        final int messageId = XtreamBytes.getWord(byteBuf, 0);
        headerSpec.messageId(messageId);

        // 2. bytes[2-3] WORD
        headerSpec.messageBodyProps(messageBodyProps);

        // 3. byte[4-9]   终端手机号或设备ID bcd[6]
        headerSpec.terminalId(XtreamBytes.getBcd(byteBuf, 4, 6));

        // 4. byte[10-11]     消息流水号
        headerSpec.flowId(XtreamBytes.getWord(byteBuf, 10));
        if (messageBodyProps.hasSubPackage()) {
            final DefaultJt808SubPackageProps subPackageProps = new DefaultJt808SubPackageProps();
            subPackageProps.totalSubPackageCount(XtreamBytes.getWord(byteBuf, 12));
            subPackageProps.currentPackageNo(XtreamBytes.getWord(byteBuf, 14));
            headerSpec.subPackageProps(subPackageProps);
        }
        return headerSpec;
    }
}
