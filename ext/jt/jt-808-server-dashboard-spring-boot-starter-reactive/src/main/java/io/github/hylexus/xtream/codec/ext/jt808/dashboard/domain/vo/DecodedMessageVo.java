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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo;

import io.github.hylexus.xtream.codec.core.tracker.RootSpan;

public class DecodedMessageVo {
    private String rawHexString;
    private String escapedHexString;
    private RootSpan details;

    public DecodedMessageVo() {
    }

    public String getRawHexString() {
        return rawHexString;
    }

    public DecodedMessageVo setRawHexString(String rawHexString) {
        this.rawHexString = rawHexString;
        return this;
    }

    public String getEscapedHexString() {
        return escapedHexString;
    }

    public DecodedMessageVo setEscapedHexString(String escapedHexString) {
        this.escapedHexString = escapedHexString;
        return this;
    }

    public RootSpan getDetails() {
        return details;
    }

    public DecodedMessageVo setDetails(RootSpan details) {
        this.details = details;
        return this;
    }
}
