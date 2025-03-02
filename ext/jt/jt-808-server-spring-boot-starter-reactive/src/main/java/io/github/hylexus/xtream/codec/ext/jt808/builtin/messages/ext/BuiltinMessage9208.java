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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext;

import io.github.hylexus.xtream.codec.core.annotation.PrependLengthFieldType;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.AlarmIdentifier;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 报警附件上传指令(苏标)
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x9208, desc = "苏标-报警附件上传指令(表-4-21)")
public class BuiltinMessage9208 {

    // STRING
    // prependLengthFieldType: 前置一个 u8 类型的字段，表示附件服务器IP地址长度
    @Preset.JtStyle.Str(prependLengthFieldType = PrependLengthFieldType.u8, desc = "附件服务器IP地址")
    private String attachmentServerIp;

    // WORD
    @Preset.JtStyle.Word(desc = "附件服务器端口（TCP）")
    private int attachmentServerPortTcp;

    // WORD
    @Preset.JtStyle.Word(desc = "附件服务器端口（UDP）")
    private int attachmentServerPortUdp;

    // BYTE[16]
    @Preset.JtStyle.Object(desc = "报警标识号(表4 16)")
    private AlarmIdentifier alarmIdentifier;

    // BYTE[32]
    @Preset.JtStyle.Bytes(desc = "报警编号:平台给报警分配的唯一编号", length = 32)
    private String alarmNo;

    // BYTE[16]
    @Preset.JtStyle.Bytes(desc = "预留")
    private String reservedByte16 = "0000000000000000";
}
