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
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808DashboardEventPayloads;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808DashboardEventType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class Jt808DashboardRequestLifecycleListener implements Jt808RequestLifecycleListener {
    private final XtreamEventPublisher eventPublisher;

    public Jt808DashboardRequestLifecycleListener(XtreamEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterRequestDecode(XtreamExchange exchange, Jt808Request jt808Request, XtreamRequest originalRequest) {
        // 请求被解码之后发送事件
        // 发送事件然后立即返回；不要有阻塞的操作
        this.eventPublisher.publishIfNecessary(
                Jt808DashboardEventType.RECEIVE_PACKAGE,
                () -> {
                    final Jt808RequestHeader header = jt808Request.header();
                    return new Jt808DashboardEventPayloads.ReceiveRequest(
                            jt808Request.requestId(),
                            jt808Request.traceId(),
                            jt808Request.terminalId(),
                            header.version().shortDesc(),
                            header.messageBodyProps().hasSubPackage(),
                            header.messageId(),
                            FormatUtils.toHexString(originalRequest.payload()),
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
                Jt808DashboardEventType.MERGE_PACKAGE,
                () -> {
                    final Jt808RequestHeader header = mergedRequest.header();
                    return new Jt808DashboardEventPayloads.MergeRequest(
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
                Jt808DashboardEventType.SEND_PACKAGE,
                () -> {
                    final Jt808Request jt808Request = (Jt808Request) request;
                    final String requestId = jt808Request.requestId();
                    final String traceId = jt808Request.traceId();
                    return new Jt808DashboardEventPayloads.SendResponse(
                            requestId,
                            traceId,
                            jt808Request.terminalId(),
                            jt808Request.messageId(),
                            FormatUtils.toHexString(response)
                    );
                }
        );
    }
}
