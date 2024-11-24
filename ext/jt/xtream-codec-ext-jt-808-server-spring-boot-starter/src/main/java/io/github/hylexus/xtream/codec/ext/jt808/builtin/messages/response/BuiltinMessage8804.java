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
 * 录音开始命令
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8804)
public class BuiltinMessage8804 {

    /**
     * 录音命令
     * <p>
     * 0：停止录音；0x01：开始录音；
     */
    @Preset.JtStyle.Byte
    private short recordingCommand;
    /**
     * 录音时间
     * <p>
     * 单位为秒（s） ，0表示一直录音
     */
    @Preset.JtStyle.Word
    private int recordingDuration;

    /**
     * 保存标志
     * <p>
     * 0：实时上传；1：保存
     */
    @Preset.JtStyle.Byte
    private short saveFlag;

    /**
     * 音频采样率
     * <p>
     * 0：8K；1：11K；2：23K；3：32K；其他保留
     */
    @Preset.JtStyle.Byte
    private short audioSampleRate;
}
