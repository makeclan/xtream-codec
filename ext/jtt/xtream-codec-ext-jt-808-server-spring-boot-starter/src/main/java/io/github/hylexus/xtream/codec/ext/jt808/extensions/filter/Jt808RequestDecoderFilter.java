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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.filter;

import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ServerType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilterChain;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import reactor.core.publisher.Mono;

/**
 * @author hylexus
 */
public class Jt808RequestDecoderFilter implements XtreamFilter {
    public static final int ORDER = -100;
    protected final Jt808RequestDecoder jt808RequestDecoder;
    protected final Jt808RequestCombiner requestCombiner;
    protected final Jt808RequestLifecycleListener lifecycleListener;

    public Jt808RequestDecoderFilter(Jt808RequestDecoder jt808RequestDecoder, Jt808RequestCombiner requestCombiner, Jt808RequestLifecycleListener lifecycleListener) {
        this.jt808RequestDecoder = jt808RequestDecoder;
        this.requestCombiner = requestCombiner;
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public Mono<Void> filter(XtreamExchange exchange, XtreamFilterChain chain) {
        final XtreamRequest originalRequest = exchange.request();
        return exchange.session().flatMap(session -> {
            final Jt808Session.MutableJt808Session jt808Session = (Jt808Session.MutableJt808Session) session;
            // 将原始的 XtreamRequest 解析为 JTT/808 格式的请求
            final Jt808Request jt808Request = this.jt808RequestDecoder.decode(
                    jt808Session.role(),
                    originalRequest.requestId(),
                    originalRequest.bufferFactory(),
                    originalRequest.underlyingInbound(),
                    originalRequest.type(),
                    originalRequest.payload(),
                    originalRequest.remoteAddress()
            );

            this.lifecycleListener.afterRequestDecode(exchange, jt808Request, originalRequest);
            this.populateSessionProperties(jt808Session, jt808Request);

            // 附件服务器，将 Session 存储到 Channel 上
            // @see Jt808AttachmentHandlerUtils.getAttachmentSessionUdp
            // @see Jt808AttachmentHandlerUtils.getAttachmentSessionTcp
            // DefaultJt808AttachmentSessionManager.beforeConnectionClose()
            if (jt808Request.serverType() == Jt808ServerType.ATTACHMENT_SERVER) {
                switch (jt808Request.type()) {
                    case TCP -> jt808Request.underlyingChannel().attr(JtProtocolConstant.tcpSessionKey()).setIfAbsent(jt808Session);
                    case UDP -> jt808Request.underlyingChannel().attr(JtProtocolConstant.udpSessionKey(jt808Request.remoteAddress())).setIfAbsent(jt808Session);
                    default -> {
                        return Mono.defer(() -> {
                            final IllegalArgumentException error = new IllegalArgumentException("Unsupported request type: " + jt808Request.type());
                            return Mono.error(error);
                        });
                    }
                }
            }

            return this.doProcessJt808Request(exchange, chain, jt808Request).doFinally(signalType -> {
                // ...
                jt808Request.release();
            });
        }).checkpoint("Jt808RequestDecoderFilter");
    }

    protected void populateSessionProperties(Jt808Session.MutableJt808Session jt808Session, Jt808Request jt808Request) {
        if (jt808Session.terminalId() == null) {
            jt808Session.terminalId(jt808Request.header().terminalId());
        }
        if (jt808Session.protocolVersion() == null) {
            jt808Session.protocolVersion(jt808Request.header().version());
        }
    }

    protected Mono<Void> doProcessJt808Request(XtreamExchange exchange, XtreamFilterChain chain, Jt808Request jt808Request) {
        // 不是子包
        if (!jt808Request.header().messageBodyProps().hasSubPackage()) {
            return chain.filter(this.mutatedExchange(exchange, jt808Request));
        }

        // 合并子包
        final Jt808Request mergedRequest = this.requestCombiner.tryMergeSubPackage(jt808Request);
        // 如果还有子包没到达，终止后续流程，直至所有子包都到达
        if (mergedRequest == null) {
            return Mono.empty();
        }

        // 所有子包都已经到达，继续执行后续流程
        final XtreamExchange mutatedExchange = this.mutatedExchange(exchange, mergedRequest);

        this.lifecycleListener.afterSubPackageMerged(mutatedExchange, mergedRequest);

        return chain.filter(mutatedExchange).doFinally(signalType -> {
            // ...
            mergedRequest.release();
        });
    }

    protected XtreamExchange mutatedExchange(XtreamExchange exchange, Jt808Request request) {
        return exchange.mutate().request(request).build();
    }

    @Override
    public int order() {
        return ORDER;
    }
}
