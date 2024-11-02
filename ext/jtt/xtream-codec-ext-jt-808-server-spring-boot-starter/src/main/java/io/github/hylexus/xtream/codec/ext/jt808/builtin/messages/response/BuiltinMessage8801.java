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
 * 摄像头立即拍摄命令
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8801)
public class BuiltinMessage8801 {
    /**
     * 通道ID > 0
     */
    @Preset.JtStyle.Byte
    private short chanelId;

    /**
     * 拍摄命令
     * <p>
     * 0 表示停止拍摄；
     * 0xFFFF 表示录像；
     * 其它表示拍照张数
     */
    @Preset.JtStyle.Word
    private int command;

    /**
     * 拍照间隔/录像时间
     * <p>
     * 秒，0表示按最小间隔拍照或一直录像
     */
    @Preset.JtStyle.Word
    private int duration;

    /**
     * 保存标志
     * <p>
     * 1：保存；
     * 0：实时上传
     */
    @Preset.JtStyle.Byte
    private short saveFlag;

    /**
     * 分辨率
     */
    @Preset.JtStyle.Byte
    private short resolution;

    /**
     * 图像/视频质量
     * <p>
     * 1-10，1代表质量损失最小，10表示压缩比最大
     */
    @Preset.JtStyle.Byte
    private short quality;

    /**
     * 亮度
     */
    @Preset.JtStyle.Byte
    private short brightness;

    /**
     * 对比度
     */
    @Preset.JtStyle.Byte
    private short contrastRate;

    /**
     * 饱和度
     */
    @Preset.JtStyle.Byte
    private short saturation;

    /**
     * 色度
     */
    @Preset.JtStyle.Byte
    private short chroma;
}
