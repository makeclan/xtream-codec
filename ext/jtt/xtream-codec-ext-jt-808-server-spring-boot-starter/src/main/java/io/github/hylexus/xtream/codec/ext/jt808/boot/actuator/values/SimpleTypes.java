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

package io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.values;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public final class SimpleTypes {
    private SimpleTypes() {
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
}
