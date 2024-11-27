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

import java.util.List;

/**
 * 事件设置
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8301)
public class BuiltinMessage8301 {
    /**
     * 事件类型
     * <li>0 -- 删除终端现有所有事件，该命令后不带后继字节</li>
     * <li>1 -- 更新事件</li>
     * <li>2 -- 追加事件</li>
     * <li>3 -- 修改事件</li>
     * <li>4 -- 删除特定几项事件，之后事件项中无需带事件内容</li>
     */
    @Preset.JtStyle.Byte
    private short eventType;

    /**
     * 设置总数
     */
    @Preset.JtStyle.Byte
    private short eventCount;

    /**
     * 事件项列表
     */
    @Preset.JtStyle.List
    private List<EventItem> eventItemList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class EventItem {

        /**
         * 事件 ID
         */
        @Preset.JtStyle.Byte
        private short eventId;

        /**
         * 事件内容长度
         */
        @Preset.JtStyle.Byte
        private short eventContentLength;

        /**
         * 事件内容(GBK)
         */
        @Preset.JtStyle.Str(lengthExpression = "getEventContentLength()")
        private String eventContent;

        public EventItem() {
        }

        public EventItem(short eventId, short eventContentLength, String eventContent) {
            this.eventId = eventId;
            this.eventContentLength = eventContentLength;
            this.eventContent = eventContent;
        }

        @SuppressWarnings("lombok")
        public short getEventContentLength() {
            return eventContentLength;
        }
    }

}
