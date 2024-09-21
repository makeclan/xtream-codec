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

import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.AlarmIdentifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 苏标-表-23 报警附件信息消息
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class BuiltinMessage1210 {

    // byte[0,7)
    @Preset.JtStyle.Bytes(length = 7)
    private String terminalId;

    // byte[7,23)
    @Preset.JtStyle.Object(length = 16)
    private AlarmIdentifier alarmIdentifier;

    // byte[23,55)
    @Preset.JtStyle.Bytes(length = 32)
    private String alarmNo;

    // byte[55]
    @Preset.JtStyle.Byte
    private short messageType;

    // byte[56]
    @Preset.JtStyle.Byte
    private short attachmentCount;

    // byte[57,...)
    @Preset.JtStyle.List
    private List<AttachmentItem> attachmentItemList;

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class AttachmentItem {
        @Preset.JtStyle.Byte
        private short length;

        @Preset.JtStyle.Str(lengthExpression = "getLength()")
        private String fileName;

        @Preset.JtStyle.Dword
        private long fileSize;

        // 这个属性不在报文里  由外部赋值
        private BuiltinMessage1210 group;
    }
}
