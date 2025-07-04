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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.jt1078;

import io.github.hylexus.xtream.codec.core.annotation.PrependLengthFieldType;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x9101, desc = "音视频实时传输请求")
public class BuiltinMessage9101 {

    // prependLengthFieldType = u8: 自动编码一个 u8 类型的字段表示IP长度字段
    @Preset.JtStyle.Str(desc = "服务器IP地址", prependLengthFieldType = PrependLengthFieldType.u8)
    private String serverIp;

    @Preset.JtStyle.Word(desc = "实时视频服务器TCP端口号")
    private int serverPortTcp;

    @Preset.JtStyle.Word(desc = "实时视频服务器UDP端口号")
    private int serverPortUdp;

    @Preset.JtStyle.Byte(desc = "逻辑通道号")
    private short channelNumber;

    @Preset.JtStyle.Byte(desc = "数据类型")
    private short dataType;

    @Preset.JtStyle.Byte(desc = "码流类型")
    private short streamType;

}
