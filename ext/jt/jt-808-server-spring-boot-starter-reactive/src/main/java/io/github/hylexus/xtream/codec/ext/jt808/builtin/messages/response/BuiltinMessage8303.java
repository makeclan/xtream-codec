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

import io.github.hylexus.xtream.codec.core.annotation.PrependLengthFieldType;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 信息点播菜单设置
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8303)
public class BuiltinMessage8303 {
    /**
     * 设置类型
     * <li>0--删除终端全部信息项</li>
     * <li>1--更新菜单</li>
     * <li>2--追加菜单</li>
     * <li>3--修改菜单</li>
     */
    @Preset.JtStyle.Byte
    private short type;

    /**
     * 信息项总数
     */
    @Preset.JtStyle.Byte
    private short itemCount;

    @Preset.JtStyle.List
    private List<Item> itemList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class Item {
        /**
         * 信息类型
         */
        @Preset.JtStyle.Byte
        private short type;

        /**
         * 信息名称
         */
        // prependLengthFieldType: 前置一个 u16(Word)类型的字段 表示 信息名称长度
        @Preset.JtStyle.Str(prependLengthFieldType = PrependLengthFieldType.u16)
        private String content;

        public Item() {
        }

        public Item(short type, String content) {
            this.type = type;
            this.content = content;
        }

    }
}
