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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.events;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionEventListener;

public class Jt808DashboardSessionCloseReason implements XtreamSessionEventListener.SessionCloseReason {
    private String reason;
    private String clientIp;

    public Jt808DashboardSessionCloseReason() {
    }

    public Jt808DashboardSessionCloseReason(String reason, String clientIp) {
        this.reason = reason;
        this.clientIp = clientIp;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    public String getClientIp() {
        return clientIp;
    }

}
