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

import io.github.hylexus.xtream.codec.server.reactive.spec.event.XtreamEvent;

/**
 * @author hylexus
 */
public enum Jt808DashboardEventType implements XtreamEvent.XtreamEventType {
    RECEIVE_PACKAGE(-100, "请求"),
    MERGE_PACKAGE(-101, "合并请求"),
    SEND_PACKAGE(-102, "响应"),
    COMMAND(-103, "指令下发"),
    ;

    private final int code;
    private final String description;

    Jt808DashboardEventType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String description() {
        return this.description;
    }
}
