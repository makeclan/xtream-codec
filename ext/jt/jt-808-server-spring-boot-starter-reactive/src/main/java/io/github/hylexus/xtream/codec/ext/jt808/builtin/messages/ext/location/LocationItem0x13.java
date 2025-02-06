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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 0x13 路线行驶时间不足/过长报警附加信息消息体数据格式
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LocationItem0x13 {
    @Preset.JtStyle.Dword(desc = "路段ID")
    private long lineId;

    @Preset.JtStyle.Word(desc = "路段行驶时间")
    private int lineDrivenTime;

    @Preset.JtStyle.Byte(desc = "结果; 0：不足；1：过长")
    private short result;
}
