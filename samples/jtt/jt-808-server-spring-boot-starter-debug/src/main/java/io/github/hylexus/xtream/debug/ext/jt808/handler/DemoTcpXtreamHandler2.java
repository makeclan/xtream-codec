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

    @Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.AUTO_DETECTION)
    @Jt808ResponseBody(messageId = 0x8001, maxPackageSize = 1000)
    public Mono<ServerCommonReplyMsg> demo1(
            XtreamExchange exchange,
            XtreamSession session,
            XtreamRequest request,
            Jt808Request jt808Request,
            DefaultXtreamRequest tcpRequest,
            XtreamResponse response,
            DefaultXtreamResponse tcpResponse,
            @Jt808RequestBody DemoLocationMsg01 msg01,
            @Jt808RequestBody DemoLocationMsg02 msg02,
            @Jt808RequestBody ByteBuf msg03) {

        assertSame(exchange.request().payload(), msg03);
        assertSame(exchange.request(), request);
        assertSame(exchange.request(), jt808Request);
        assertSame(exchange.request(), tcpRequest);
        assertSame(exchange.response(), response);
        assertSame(exchange.response(), tcpResponse);
        final ServerCommonReplyMsg responseBody = ServerCommonReplyMsg.success(0x0200, jt808Request.header().flowId());
        // return Mono.empty();
        return Mono.just(responseBody);
    }

}
