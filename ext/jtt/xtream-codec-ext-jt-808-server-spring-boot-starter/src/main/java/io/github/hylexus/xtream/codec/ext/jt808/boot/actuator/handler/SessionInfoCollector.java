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

package io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.handler;

import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.values.Jt808ServerSimpleMetricsHolder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionEventListener;

public class SessionInfoCollector implements XtreamSessionEventListener {
    private final Jt808ServerSimpleMetricsHolder metricsHolder;
    private final Jt808SessionManager sessionManager;

    public SessionInfoCollector(Jt808ServerSimpleMetricsHolder metricsHolder, Jt808SessionManager sessionManager) {
        this.metricsHolder = metricsHolder;
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterSessionCreate(XtreamSession session) {
        updateCount(session.type());
    }

    @Override
    public void beforeSessionClose(XtreamSession session, SessionCloseReason reason) {
        updateCount(session.type());
    }

    private void updateCount(XtreamInbound.Type type) {
        final long count = this.sessionManager.count();
        if (type == XtreamInbound.Type.TCP) {
            metricsHolder.getTcpInstructionSession().setMax(count);
            metricsHolder.getTcpInstructionSession().setCurrent(count);
        } else {
            metricsHolder.getUdpInstructionSession().setMax(count);
            metricsHolder.getUdpInstructionSession().setCurrent(count);
        }
    }

}
