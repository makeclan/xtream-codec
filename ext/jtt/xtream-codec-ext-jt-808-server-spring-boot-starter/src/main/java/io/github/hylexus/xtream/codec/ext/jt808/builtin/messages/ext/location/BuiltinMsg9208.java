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
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x9208)
public class BuiltinMsg9208 {

    // BYTE
    @Preset.JtStyle.Byte
    private short attachmentServerIpLength;

    // STRING
    @Preset.JtStyle.Str
    private String attachmentServerIp;

    // WORD
    @Preset.JtStyle.Word
    private int attachmentServerPortTcp;

    // WORD
    @Preset.JtStyle.Word
    private int attachmentServerPortUdp;

    // BYTE[16]
    @Preset.JtStyle.Object
    private AlarmIdentifier alarmIdentifier;

    // BYTE[32]
    @Preset.JtStyle.Bytes
    private String alarmNo;

    // BYTE[16]
    @Preset.JtStyle.Bytes
    private String reservedByte16 = "0000000000000000";
}
