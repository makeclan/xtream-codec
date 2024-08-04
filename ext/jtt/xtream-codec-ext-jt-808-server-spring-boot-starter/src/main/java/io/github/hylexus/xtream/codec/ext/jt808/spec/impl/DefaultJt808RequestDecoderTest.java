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

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.common.utils.XtreamUtils;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.netty.NettyInbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultJt808RequestDecoderTest {
    final ByteBufAllocator allocator = XtreamUtils.DEFAULT_BUFFER_FACTORY;
    NettyInbound nettyInbound;
    DefaultJt808RequestDecoder decoder;

    @BeforeEach
    void setUp() {
        this.nettyInbound = mock(NettyInbound.class);
        when(nettyInbound.withConnection(any())).thenReturn(nettyInbound);
        this.decoder = new DefaultJt808RequestDecoder(new DefaultJt808MsgBytesProcessor(allocator));
    }

    @Test
    void test() {
        final String hexString = "02004086010000000001893094655200E4000000000000000101D907F2073D336C000000000000211124114808010400000026030200003001153101002504000000001404000000011504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B01711800000000000000000000000000000000000000000000000056";
        final Jt808Request request = doDecode(hexString);
        assertEquals(0x0200, request.header().msgId());
        assertEquals(Jt808ProtocolVersion.VERSION_2019, request.header().version());
        assertEquals("00000000018930946552", request.header().terminalId());
        assertEquals(228, request.header().flowId());
        final Jt808RequestHeader.Jt808MsgBodyProps bodyProps = request.header().msgBodyProps();
        assertEquals(16518, bodyProps.intValue());
        assertEquals(134, bodyProps.msgBodyLength());
        assertFalse(bodyProps.hasSubPackage());
        assertEquals(0, bodyProps.encryptionType());
    }

    private Jt808Request doDecode(String hexString) {
        final ByteBuf payload = XtreamBytes.byteBufFromHexString(allocator, hexString);
        return decoder.decode(allocator, nettyInbound, payload);
    }
}
