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

package io.github.hylexus.xtream.codec.server.reactive.spec.domain.values;


import java.time.Duration;
import java.util.StringJoiner;

@SuppressWarnings("all")
public class SessionIdleStateCheckerProps {
    private boolean enabled = true;
    private Duration maxIdleTime = Duration.ofMinutes(10);
    private Duration checkInterval = Duration.ofSeconds(60);
    private Duration checkBackoffTime = Duration.ofSeconds(10);

    public boolean isEnabled() {
        return enabled;
    }

    public SessionIdleStateCheckerProps setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public SessionIdleStateCheckerProps setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public Duration getCheckInterval() {
        return checkInterval;
    }

    public SessionIdleStateCheckerProps setCheckInterval(Duration checkInterval) {
        this.checkInterval = checkInterval;
        return this;
    }

    public Duration getCheckBackoffTime() {
        return checkBackoffTime;
    }

    public SessionIdleStateCheckerProps setCheckBackoffTime(Duration checkBackoffTime) {
        this.checkBackoffTime = checkBackoffTime;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SessionIdleStateCheckerProps.class.getSimpleName() + "[", "]")
                .add("enabled=" + enabled)
                .add("maxIdleTime=" + maxIdleTime)
                .add("checkInterval=" + checkInterval)
                .add("checkBackoffTime=" + checkBackoffTime)
                .toString();
    }
}
