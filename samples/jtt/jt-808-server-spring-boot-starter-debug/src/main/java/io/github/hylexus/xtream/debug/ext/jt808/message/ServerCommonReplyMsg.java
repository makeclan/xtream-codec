/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
