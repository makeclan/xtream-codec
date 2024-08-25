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

package io.github.hylexus.xtream.debug.ext.jt808.handler;

import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.*;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8100;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.ServerCommonReplyMessage;
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
public class DemoJt808RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(DemoJt808RequestHandler.class);

    public DemoJt808RequestHandler() {
    }

    /**
     * 终端鉴权(V2019)
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0102, versions = Jt808ProtocolVersion.VERSION_2019)
    @Jt808ResponseBody(messageId = 0x8001)
    public Mono<ServerCommonReplyMessage> processMessage0102V2019(Jt808Request request, @Jt808RequestBody BuiltinMessage0102V2019 requestBody) {
        log.info("receive message [0x0100-v2019]: {}", requestBody);
        final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(request);
        return Mono.just(responseBody);
    }

    /**
     * 终端鉴权(V2011 or V2013)
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0102, versions = {Jt808ProtocolVersion.VERSION_2011, Jt808ProtocolVersion.VERSION_2013})
    @Jt808ResponseBody(messageId = 0x8001)
    public Mono<ServerCommonReplyMessage> processMessage0102(Jt808Request request, @Jt808RequestBody BuiltinMessage0102V2013 requestBody) {
        log.info("receive message [0x0100-(v2011 or v2013)]: {}", requestBody);
        final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(request);
        return Mono.just(responseBody);
    }

    /**
     * 位置上报(V2019)
     * <p>
     * 7e02004086010000000001893094655200E4000000000000000101D907F2073D336C000000000000211124114808010400000026030200003001153101002504000000001404000000011504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B017118000000000000000000000000000000000000000000000000567e
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
    @Jt808ResponseBody(messageId = 0x8001, maxPackageSize = 1000)
    public Mono<ServerCommonReplyMessage> processMessage0200V2019(
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
        final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(jt808Request);
        // return Mono.empty();
        return Mono.just(responseBody);
    }

    /**
     * 终端注册(V2019)
     * <p>
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
    public Mono<BuiltinMessage8100> processMessage0x0100V2019(Jt808Request request, @Jt808RequestBody BuiltinMessage0100V2019 requestBody) {
        log.info("receive message [0x0100-v2019]: {}", requestBody);
        log.info("{}", Thread.currentThread());
        final BuiltinMessage8100 builtinMessage8100 = new BuiltinMessage8100()
                .setClientFlowId(request.header().flowId())
                .setResult((short) 0)
                .setAuthCode("auth-code-2019");

        return Mono.just(builtinMessage8100);
    }

    /**
     * 终端注册(V2013)
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0100, versions = Jt808ProtocolVersion.VERSION_2013)
    @Jt808ResponseBody(messageId = 0x8100, maxPackageSize = 1000)
    public Mono<BuiltinMessage8100> processMessage0x0100V2013(Jt808Request request, @Jt808RequestBody BuiltinMessage0100V2013 requestBody) {
        log.info("receive message [0x0100-v2013]: {}", requestBody);
        final BuiltinMessage8100 builtinMessage8100 = new BuiltinMessage8100()
                .setClientFlowId(request.header().flowId())
                .setResult((short) 0)
                .setAuthCode("auth-code-2013");

        return Mono.just(builtinMessage8100);
    }

    /**
     * 终端注册(V2011)
     */
    @Jt808RequestHandlerMapping(messageIds = 0x0100, versions = Jt808ProtocolVersion.VERSION_2011)
    @Jt808ResponseBody(messageId = 0x8100, maxPackageSize = 1000)
    public Mono<BuiltinMessage8100> processMessage0x0100V2011(Jt808Request request, @Jt808RequestBody BuiltinMessage0100V2011 requestBody) {
        log.info("receive message [0x0100-v2011]: {}", requestBody);
        final BuiltinMessage8100 builtinMessage8100 = new BuiltinMessage8100()
                .setClientFlowId(request.header().flowId())
                .setResult((short) 0)
                .setAuthCode("auth-code-2011");

        return Mono.just(builtinMessage8100);
    }

    @Jt808RequestHandlerMapping(messageIds = 0x0002)
    @Jt808ResponseBody(messageId = 0x8001)
    public Mono<ServerCommonReplyMessage> processMessage0002(Jt808Request request, @Jt808RequestBody BuiltinMessage0002 requestBody) {
        log.info("receive message [0x0002]: {}", requestBody);
        final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(request);
        return Mono.just(responseBody);
    }
}
