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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto;

public enum LinkDataType {
    /**
     * 上行(完整包 或 子包)
     */
    REQUEST,
    /**
     * 上行(子包合并之后)
     */
    MERGED_REQUEST,
    /**
     * 响应
     */
    RESPONSE,
    /**
     * 指令(主动下发)
     */
    COMMAND,
    /**
     * 所有
     */
    ALL,
}
