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
import io.github.hylexus.xtream.codec.core.type.wrapper.DwordWrapper;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查询区域或线路数据
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8608)
public class BuiltinMessage8608 {

    /**
     * 查询类型
     * <li>1 -- 查询圆形区域数据</li>
     * <li>2 -- 查询矩形区域数据</li>
     * <li>3 -- 查询多边形区域数据</li>
     * <li>4 -- 查询线路数据</li>
     */
    @Preset.JtStyle.Byte
    private short type;

    /**
     * 要查询的区域或线路的ID数量
     */
    @Preset.JtStyle.Dword
    private long count;

    @Preset.JtStyle.List
    private List<DwordWrapper> idList;
}
