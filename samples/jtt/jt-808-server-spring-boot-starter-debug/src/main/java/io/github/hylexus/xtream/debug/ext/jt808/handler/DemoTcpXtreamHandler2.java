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

package io.github.hylexus.xtream.debug.ext.jt808.handler;

import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.BuiltinMsg0100V2011;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.BuiltinMsg0100V2013;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.BuiltinMsg0100V2019;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.BuiltinMsg8100;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808RequestBody;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808RequestHandler;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamResponse;
import io.github.hylexus.xtream.debug.ext.jt808.message.DemoLocationMsg01;
import io.github.hylexus.xtream.debug.ext.jt808.message.DemoLocationMsg02;
import io.github.hylexus.xtream.debug.ext.jt808.message.ServerCommonReplyMsg;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.github.hylexus.xtream.codec.common.utils.XtreamAssertions.assertNotSame;
import static io.github.hylexus.xtream.codec.common.utils.XtreamAssertions.assertSame;

/**
 * @author hylexus
 */
@Component
@Jt808RequestHandler
public class DemoTcpXtreamHandler2 {

    private static final Logger log = LoggerFactory.getLogger(DemoTcpXtreamHandler2.class);

    public DemoTcpXtreamHandler2() {
    }

    /**
     * 7e02004086010000000001893094655200E4000000000000000101D907F2073D336C000000000000211124114808010400000026030200003001153101002504000000001404000000011504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B017118000000000000000000000000000000000000000000000000567e
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
    @Jt808ResponseBody(messageId = 0x8001, maxPackageSize = 1000)
    public Mono<ServerCommonReplyMsg> processMessage0200V2019(
            XtreamExchange exchange,
            XtreamSession xtreamSession,
            XtreamRequest xtreamRequest,
            Jt808Request jt808Request,
            DefaultXtreamRequest defaultXtreamRequest,
            XtreamResponse xtreamResponse,
            DefaultXtreamResponse defaultXtreamResponse,
            @Jt808RequestBody DemoLocationMsg01 msg01,
            @Jt808RequestBody DemoLocationMsg02 msg02,
            @Jt808RequestBody ByteBuf buf01,
            @Jt808RequestBody ByteBuf buf02,
            @Jt808RequestBody(bufferAsSlice = false) ByteBuf buf03,
            @Jt808RequestBody(bufferAsSlice = false) ByteBuf buf04) {

        log.info("v2019-0x0200: {}", msg01);
        assertNotSame(buf01, buf02);
        assertNotSame(buf01, buf03);
        assertSame(buf03, buf04);
        assertSame(exchange.request(), xtreamRequest);
        assertSame(exchange.request(), jt808Request);
        assertSame(exchange.request(), defaultXtreamRequest);
        assertSame(exchange.response(), xtreamResponse);
        assertSame(exchange.response(), defaultXtreamResponse);
        final ServerCommonReplyMsg responseBody = ServerCommonReplyMsg.success(0x0200, jt808Request.header().flowId());
        // return Mono.empty();
        return Mono.just(responseBody);
    }

    /**
     * 不分包:
     * <p>
     * 7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E
     * <p>
     * 分包:
     * <p>
     * 7E010060240100000000013912344329000000030001000B00026964393837363534333231747970653030313233343536373831323334353637277E
     * 7E010060240100000000013912344329000100030002383837363534333231494430303030313233343536373831323334353637383837363534357E
     * 7E0100600E010000000001391234432900020003000333323101B8CA4A2D313233343539347E
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0100, versions = Jt808ProtocolVersion.VERSION_2019)
    @Jt808ResponseBody(messageId = 0x8100, maxPackageSize = 1000)
    public Mono<BuiltinMsg8100> processMessage0x0100V2019(Jt808Request request, @Jt808RequestBody BuiltinMsg0100V2019 requestBody) {
        log.info("receive message [0x0100-v2019]: {}", requestBody);
        log.info("{}", Thread.currentThread());
        final BuiltinMsg8100 builtinMsg8100 = new BuiltinMsg8100()
                .setTerminalFlowId(request.header().flowId())
                .setResult((short) 0)
                .setAuthCode("auth-code-2019");

        return Mono.just(builtinMsg8100);
    }

    @Jt808RequestHandlerMapping(messageIds = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    @Jt808ResponseBody(messageId = 0x8100, maxPackageSize = 1000)
    public Mono<BuiltinMsg8100> processMessage0x0100V2013(Jt808Request request, @Jt808RequestBody BuiltinMsg0100V2013 requestBody) {
        log.info("receive message [0x0100-v2013]: {}", requestBody);
        final BuiltinMsg8100 builtinMsg8100 = new BuiltinMsg8100()
                .setTerminalFlowId(request.header().flowId())
                .setResult((short) 0)
                .setAuthCode("auth-code-2013");

        return Mono.just(builtinMsg8100);
    }

    @Jt808RequestHandlerMapping(messageIds = 0x0100, versions = Jt808ProtocolVersion.VERSION_2011)
    @Jt808ResponseBody(messageId = 0x8100, maxPackageSize = 1000)
    public Mono<BuiltinMsg8100> processMessage0x0100V2011(Jt808Request request, @Jt808RequestBody BuiltinMsg0100V2011 requestBody) {
        log.info("receive message [0x0100-v2011]: {}", requestBody);
        final BuiltinMsg8100 builtinMsg8100 = new BuiltinMsg8100()
                .setTerminalFlowId(request.header().flowId())
                .setResult((short) 0)
                .setAuthCode("auth-code-2011");

        return Mono.just(builtinMsg8100);
    }

}
