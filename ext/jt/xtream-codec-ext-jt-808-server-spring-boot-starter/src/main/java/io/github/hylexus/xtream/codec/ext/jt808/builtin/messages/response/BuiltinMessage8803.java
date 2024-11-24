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

import java.time.LocalDateTime;

/**
 * 存储多媒体数据上传命令
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8803)
public class BuiltinMessage8803 {

    /**
     * 多媒体类型
     * <p>
     * 0：图像；1：音频；2：视频；
     */
    @Preset.JtStyle.Byte
    private short multimediaType;
    /**
     * 通道ID
     * <p>
     * 0表示检索该媒体类型的所有通道；
     */
    @Preset.JtStyle.Byte
    private short channelId;

    /**
     * 事件项编码
     */
    @Preset.JtStyle.Byte
    private short eventItemCode;

    /**
     * 起始时间
     */
    @Preset.JtStyle.BcdDateTime
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Preset.JtStyle.BcdDateTime
    private LocalDateTime endTime;

    /**
     * 删除标志
     * <p>
     * 0：保留；1：删除；
     */
    @Preset.JtStyle.Byte
    private short deleteFlag;
}
