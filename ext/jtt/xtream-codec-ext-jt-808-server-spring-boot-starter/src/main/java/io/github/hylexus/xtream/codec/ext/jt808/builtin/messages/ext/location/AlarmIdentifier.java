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
import io.github.hylexus.xtream.codec.ext.jt808.extensions.annotation.JtExtension;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 苏标-表-4-16 报警标识号格式
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
public class AlarmIdentifier {
    // 终端ID BYTE[7] 7个字节，由大写字母和数字组成
    @Preset.JtStyle.Str(length = 7)
    private String terminalId;

    // 时间   BCD[6]  YY-MM-DD-hh-mm-ss （GMT+8时间）
    @JtExtension.BcdDateTime
    // 或者
    // @XtreamDateTimeField(pattern = "yyMMddHHmmss", length = 6, charset = "bcd_8421")
    private LocalDateTime time;

    // 序号   BYTE    同一时间点报警的序号，从0循环累加
    @Preset.JtStyle.Byte
    private short sequence;

    // 附件数量 BYTE    表示该报警对应的附件数量
    @Preset.JtStyle.Byte
    private short attachmentCount;

    // 预留 BYTE
    @Preset.JtStyle.Byte
    private short reserved = 0;
}
