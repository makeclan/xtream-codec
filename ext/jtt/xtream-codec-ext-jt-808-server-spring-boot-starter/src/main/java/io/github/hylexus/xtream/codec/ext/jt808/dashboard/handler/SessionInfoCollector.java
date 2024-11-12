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

import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.events.Jt808DashboardEventPayloads;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808ServerSimpleMetricsHolder;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.SimpleTypes;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808AttachmentSessionManager;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ServerType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionEventListener;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEvent;
import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEventPublisher;

public class SessionInfoCollector implements XtreamSessionEventListener {
    private final Jt808ServerSimpleMetricsHolder metricsHolder;
    private final Jt808SessionManager sessionManager;
    private final Jt808AttachmentSessionManager attachmentSessionManager;
    private final XtreamEventPublisher eventPublisher;

    public SessionInfoCollector(Jt808ServerSimpleMetricsHolder metricsHolder, Jt808SessionManager sessionManager, Jt808AttachmentSessionManager attachmentSessionManager, XtreamEventPublisher eventPublisher) {
        this.metricsHolder = metricsHolder;
        this.sessionManager = sessionManager;
        this.attachmentSessionManager = attachmentSessionManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterSessionCreate(XtreamSession session) {
        final Jt808Session jt808Session = (Jt808Session) session;

        updateCount(session.type(), jt808Session.role());

        this.eventPublisher.publishIfNecessary(
                XtreamEvent.XtreamEventType.AFTER_SESSION_CREATED,
                () -> {
                    // ...
                    return new Jt808DashboardEventPayloads.SessionCreationInfo(
                            jt808Session.id(),
                            jt808Session.terminalId(),
                            jt808Session.protocolVersion().shortDesc(),
                            jt808Session.type().name(),
                            jt808Session.remoteAddress().toString()
                    );
                }
        );
    }

    @Override
    public void beforeSessionClose(XtreamSession session, SessionCloseReason reason) {
        final Jt808Session jt808Session = (Jt808Session) session;

        updateCount(session.type(), jt808Session.role());

        this.eventPublisher.publishIfNecessary(
                XtreamEvent.XtreamEventType.BEFORE_SESSION_CLOSED,
                () -> {
                    // ...
                    return new Jt808DashboardEventPayloads.SessionClosingInfo(
                            jt808Session.id(),
                            jt808Session.terminalId(),
                            jt808Session.protocolVersion().shortDesc(),
                            jt808Session.type().name(),
                            jt808Session.remoteAddress().toString(),
                            reason
                    );
                }
        );
    }

    private void updateCount(XtreamInbound.Type type, Jt808ServerType role) {
        switch (role) {
            case INSTRUCTION_SERVER -> {
                final long count = this.sessionManager.count();
                switch (type) {
                    case TCP -> this.doUpdateCount(metricsHolder.getTcpInstructionSession(), count);
                    case UDP -> this.doUpdateCount(metricsHolder.getUdpInstructionSession(), count);
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }
            }
            case ATTACHMENT_SERVER -> {
                final long count = this.attachmentSessionManager.count();
                switch (type) {
                    case TCP -> this.doUpdateCount(metricsHolder.getTcpAttachmentSession(), count);
                    case UDP -> this.doUpdateCount(metricsHolder.getUdpAttachmentSession(), count);
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + role);
        }
    }

    private void doUpdateCount(SimpleTypes.SessionInfo sessionInfo, long count) {
        sessionInfo.setMax(count);
        sessionInfo.setCurrent(count);
    }

}
