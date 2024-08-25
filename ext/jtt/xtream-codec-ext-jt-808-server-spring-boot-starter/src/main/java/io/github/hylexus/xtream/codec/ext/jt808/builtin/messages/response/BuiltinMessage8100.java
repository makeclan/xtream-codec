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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 终端注册应答
 *
 * @author hylexus
 * @see io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0100V2011
 * @see io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0100V2013
 * @see io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0100V2019
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage8100 {

    // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    @Preset.JtStyle.Word
    private int clientFlowId;

    // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    @Preset.JtStyle.Byte
    private short result;

    // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    @Preset.JtStyle.Str(condition = "result == 0")
    private String authCode;

}
