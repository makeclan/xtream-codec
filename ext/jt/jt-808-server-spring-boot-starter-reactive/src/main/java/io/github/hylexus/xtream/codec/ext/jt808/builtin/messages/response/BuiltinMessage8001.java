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
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 平台通用应答 0x8001
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8001, desc = "平台通用应答")
public class BuiltinMessage8001 {

    @Preset.JtStyle.Word(desc = "应答流水号; 对应终端消息的流水号")
    protected int clientFlowId;

    @Preset.JtStyle.Word(desc = "应答id; 对应终端消息的ID")
    protected int clientMessageId;

    @Preset.JtStyle.Byte(desc = "结果; 0:成功/确认; 1:失败; 2:消息有误; 3:不支持; 4:报警处理确认")
    protected short result;

}
