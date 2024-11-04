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

package io.github.hylexus.xtream.codec.ext.jt808.boot.actuator;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestLifecycleListener;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionEventListener;
import org.springframework.boot.actuate.endpoint.OperationResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public record Jt808ServerSimpleMetricsHolder(SessionInfo sessions, RequestInfo requests) implements OperationResponseBody {

    @SuppressWarnings("all")
    public static final class SessionInfo {
        private long max;
        private long current;

        public SessionInfo() {
            this.max = 0;
            this.current = 0;
        }

        public void setMax(long value) {
            this.max = Math.max(this.max, value);
        }

        public long getMax() {
            return max;
        }

        public long getCurrent() {
            return current;
        }

        public SessionInfo setCurrent(long current) {
            this.current = current;
            return this;
        }
    }

    public record RequestInfo(LongAdder total, Map<String, RequestDetail> details) {
        public void incrementTotal() {
            this.total.increment();
        }
    }

    public record RequestDetail(int messageId, LongAdder count, String desc) {
        public void incrementCount() {
            this.count.increment();
        }

        // for jackson
        @SuppressWarnings("unused")
        public String getMessageIdAsHexString() {
            return "0x" + FormatUtils.toHexString(messageId, 4);
        }
    }

    public static class RequestInfoCollector implements Jt808RequestLifecycleListener {
        private final Jt808ServerSimpleMetricsHolder metricsHolder;
        public static final Map<Integer, String> MESSAGE_ID_DESC_MAPPING;

        static {
            MESSAGE_ID_DESC_MAPPING = new HashMap<>();
            MESSAGE_ID_DESC_MAPPING.put(0x0100, "终端心跳");
            MESSAGE_ID_DESC_MAPPING.put(0x0102, "终端注销");
            MESSAGE_ID_DESC_MAPPING.put(0x0200, "定位数据上报");
        }

        public RequestInfoCollector(Jt808ServerSimpleMetricsHolder metricsHolder) {
            this.metricsHolder = metricsHolder;
        }

        @Override
        public void afterRequestDecode(XtreamExchange exchange, Jt808Request jt808Request, XtreamRequest originalRequest) {
            final int messageId = jt808Request.header().messageId();
            this.metricsHolder.requests().incrementTotal();
            this.metricsHolder.requests()
                    .details()
                    .computeIfAbsent("message_" + messageId, k -> new RequestDetail(messageId, new LongAdder(), MESSAGE_ID_DESC_MAPPING.getOrDefault(messageId, "Unknown")))
                    .incrementCount();
        }

    }

    public static class SessionInfoCollector implements XtreamSessionEventListener {
        private final Jt808ServerSimpleMetricsHolder metricsHolder;
        private final Jt808SessionManager sessionManager;

        public SessionInfoCollector(Jt808ServerSimpleMetricsHolder metricsHolder, Jt808SessionManager sessionManager) {
            this.metricsHolder = metricsHolder;
            this.sessionManager = sessionManager;
        }

        @Override
        public void afterSessionCreate(XtreamSession session) {
            final long count = this.sessionManager.count();
            metricsHolder.sessions().setMax(count);
            metricsHolder.sessions().setCurrent(count);
        }

        @Override
        public void beforeSessionClose(XtreamSession session, SessionCloseReason reason) {
            final long count = sessionManager.count();
            metricsHolder.sessions().setCurrent(count);
        }

    }
}
