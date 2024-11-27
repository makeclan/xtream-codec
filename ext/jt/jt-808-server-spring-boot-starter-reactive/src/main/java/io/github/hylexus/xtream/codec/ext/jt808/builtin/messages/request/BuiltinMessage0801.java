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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request;

import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 多媒体数据上传
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x0801)
public class BuiltinMessage0801 {

    /**
     * 多媒体数据ID > 0
     */
    @Preset.JtStyle.Dword
    private long multimediaDataID;

    /**
     * 多媒体类型
     * <p>
     * 0：图像；1：音频；2：视频；
     */
    @Preset.JtStyle.Byte
    private short multimediaType;

    /**
     * 多媒体格式编码
     * <li>0：JPEG</li>
     * <li>1：TIF</li>
     * <li>2：MP3</li>
     * <li>3：WAV</li>
     * <li>4：WMV</li>
     * 其他保留
     */
    @Preset.JtStyle.Byte
    private short multimediaFormatCode;

    /**
     * 事件项编码
     * <li>0：平台下发指令</li>
     * <li>1：定时动作</li>
     * <li>2：抢劫报警触发</li>
     * <li>3：碰撞侧翻报警触发</li>
     * <li>4：门开拍照</li>
     * <li>5：门关拍照</li>
     * <li>6：车门由开变关，时速从＜20公里到超过20公里</li>
     * <li>7：定距拍照</li>
     * 其他保留
     */
    @Preset.JtStyle.Byte
    private short eventItemCode;

    /**
     * 通道ID
     */
    @Preset.JtStyle.Byte
    private short channelId;

    /**
     * 位置信息汇报(0x0200)消息体  BYTE[28]
     * <p>
     * 表示多媒体数据的位置基本信息数据
     */
    @Preset.JtStyle.Object(length = 28)
    private BuiltinMessage0200 location;

    /**
     * 多媒体数据包
     */
    @Preset.JtStyle.Bytes
    private byte[] mediaData;
}
