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
 * 设置电话本
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8401)
public class BuiltinMessage8401 {

    /**
     * 设置类型
     * <li>0 -- 删除终端上所有存储的联系人</li>
     * <li>1 -- 表示更新电话本（删除终端中已有全部联系人并追加消息中的联系人） </li>
     * <li>2 -- 表示追加电话本</li>
     * <li>3 -- 表示修改电话本（以联系人为索引）</li>
     */
    @Preset.JtStyle.Byte
    private short type;

    /**
     * 联系人总数
     */
    @Preset.JtStyle.Byte
    private short count;

    @Preset.JtStyle.List
    private List<Item> itemList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class Item {
        /**
         * 标志
         * <p>
         * 1：呼入；2：呼出；3：呼入/呼出
         */
        @Preset.JtStyle.Byte
        private short flag;

        /**
         * 电话号码
         */
        // prependLengthFieldType: 前置一个 u8类型的字段 表示 号码长度
        @Preset.JtStyle.Str(prependLengthFieldType = PrependLengthFieldType.u8)
        private String phoneNumber;

        /**
         * 联系人
         */
        // prependLengthFieldType: 前置一个 u8类型的字段 表示 联系人长度
        @Preset.JtStyle.Str(prependLengthFieldType = PrependLengthFieldType.u8)
        private String contacts;

    }
}
