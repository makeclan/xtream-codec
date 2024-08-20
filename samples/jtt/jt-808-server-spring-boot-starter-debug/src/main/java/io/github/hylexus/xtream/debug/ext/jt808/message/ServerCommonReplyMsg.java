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

package io.github.hylexus.xtream.debug.ext.jt808.message;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServerCommonReplyMsg {
    // 成功/确认
    public static final short RESULT_SUCCESS = 0;
    // 失败
    public static final short RESULT_FAILURE = 1;
    // 消息有误
    public static final short RESULT_MSG_ERROR = 2;
    // 不支持
    public static final short RESULT_UNSUPPORTED = 3;
    // 报警处理确认
    public static final short RESULT_ALARM_MSG_ACK = 4;

    // 1. 应答流水号 WORD    对应的终端消息的流水号
    @Preset.RustStyle.u16
    private int clientFlowId;

    // 2. 应答id WORD     对应的终端消息的 ID
    @Preset.RustStyle.u16
    private int clientMsgType;

    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @Preset.RustStyle.u8
    private short result;

    public static ServerCommonReplyMsg success(int clientMsgType, int clientFlowId) {
        return new ServerCommonReplyMsg()
                .setResult(RESULT_SUCCESS)
                .setClientMsgType(clientMsgType)
                .setClientFlowId(clientFlowId);
    }
}
