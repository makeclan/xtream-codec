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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage9212 {

    // BYTE
    @Preset.JtStyle.Byte
    private short fileNameLength;

    // 文件名称
    @Preset.JtStyle.Str
    private String fileName;

    // 0x00：图片
    // 0x01：音频
    // 0x02：视频
    // 0x03：文本
    // 0x04：其它
    @Preset.JtStyle.Byte
    private short fileType;

    // 0x00：完成
    // 0x01：需要补传
    @Preset.JtStyle.Byte
    private short uploadResult;

    // 补传数据包数量 需要补传的数据包数量，无补传时该值为0
    @Preset.JtStyle.Byte
    private short packageCountToReTransmit;

    // 补传数据包列表
    @Preset.JtStyle.List
    private List<RetransmitItem> retransmitItemList;

    @Data
    public static class RetransmitItem {
        // 数据偏移量
        @Preset.JtStyle.Dword
        private long dataOffset;
        // 数据长度
        @Preset.JtStyle.Dword
        private long dataLength;
    }
}
