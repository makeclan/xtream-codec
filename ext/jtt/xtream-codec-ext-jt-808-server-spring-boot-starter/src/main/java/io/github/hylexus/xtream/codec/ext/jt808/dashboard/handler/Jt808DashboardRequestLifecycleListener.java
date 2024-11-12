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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.handler;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.events.Jt808DashboardEventPayloads;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.events.Jt808DashboardEventType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEvent;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import io.netty.buffer.ByteBuf;
import reactor.netty.NettyInbound;

/**
 * @author hylexus
 */
public class Jt808DashboardRequestLifecycleListener implements Jt808RequestLifecycleListener {
    private final XtreamEventPublisher eventPublisher;

    public Jt808DashboardRequestLifecycleListener(XtreamEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterRequestDecode(NettyInbound nettyInbound, ByteBuf rawPayload, Jt808Request jt808Request) {
        // 请求被解码之后发送事件
        // 发送事件然后立即返回；不要有阻塞的操作
        this.eventPublisher.publishIfNecessary(
                XtreamEvent.XtreamEventType.AFTER_REQUEST_RECEIVED,
                () -> {
                    final Jt808RequestHeader header = jt808Request.header();
                    return new Jt808DashboardEventPayloads.ReceivedRequestInfo(
                            jt808Request.requestId(),
                            jt808Request.traceId(),
                            jt808Request.terminalId(),
                            header.version().shortDesc(),
                            header.messageBodyProps().hasSubPackage(),
                            header.messageId(),
                            FormatUtils.toHexString(rawPayload),
                            FormatUtils.toHexString(jt808Request.payload())
                    );
                }
        );
    }

    @Override
    public void afterSubPackageMerged(XtreamExchange exchange, Jt808Request mergedRequest) {
        // 分包合并之后发送事件
        // 发送事件然后立即返回；不要有阻塞的操作
        this.eventPublisher.publishIfNecessary(
                Jt808DashboardEventType.AFTER_SUB_REQUEST_MERGED,
                () -> {
                    final Jt808RequestHeader header = mergedRequest.header();
                    return new Jt808DashboardEventPayloads.MergedRequestInfo(
                            mergedRequest.requestId(),
                            mergedRequest.traceId(),
                            mergedRequest.terminalId(),
                            header.version().shortDesc(),
                            header.messageBodyProps().hasSubPackage(),
                            header.messageId(),
                            FormatUtils.toHexString(mergedRequest.payload())
                    );
                }
        );
    }

    @Override
    public void beforeResponseSend(XtreamRequest request, ByteBuf response) {
        this.eventPublisher.publishIfNecessary(
                XtreamEvent.XtreamEventType.BEFORE_RESPONSE_SEND,
                () -> {
                    final Jt808Request jt808Request = (Jt808Request) request;
                    final String requestId = jt808Request.requestId();
                    final String traceId = jt808Request.traceId();
                    return new Jt808DashboardEventPayloads.ResponseInfo(
                            requestId,
                            traceId,
                            jt808Request.terminalId(),
                            jt808Request.messageId(),
                            FormatUtils.toHexString(response)
                    );
                }
        );
    }

    @Override
    public void beforeCommandSend(Jt808Session session, ByteBuf command) {
        this.eventPublisher.publishIfNecessary(
                XtreamEvent.XtreamEventType.BEFORE_COMMAND_SEND,
                () -> {
                    // ...
                    return new Jt808DashboardEventPayloads.CommandInfo(
                            session.id(),
                            session.terminalId(),
                            session.protocolVersion().shortDesc(),
                            FormatUtils.toHexString(command)
                    );
                }
        );
    }
}
