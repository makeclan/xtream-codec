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

import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.codec.CustomParameterListFieldCodec;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.codec.ParameterItem;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查询终端参数应答 0x0104
 *
 * @author hylexus
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Jt808ResponseBody(messageId = 0x0104, desc = "查询终端参数应答")
public class BuiltinMessage0104Sample2 {
    /**
     * 对应的终端参数查询消息的流水号
     */
    @Preset.JtStyle.Word
    private int flowId;

    /**
     * 应答参数个数
     */
    @Preset.JtStyle.Byte
    private short parameterCount;

    /**
     * 参数项列表: 见表 10
     */
    @XtreamField(fieldCodec = CustomParameterListFieldCodec.class)
    private List<ParameterItem> parameterItems;

}
