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

import io.github.hylexus.xtream.codec.core.annotation.PrependLengthFieldType;
import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 定位数据批量上传
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage0704 {

    /**
     * 数据项个数
     */
    @Preset.JtStyle.Word
    private int count;

    /**
     * 位置数据类型
     * <li>0：正常位置批量汇报</li>
     * <li>1：盲区补报</li>
     */
    @Preset.JtStyle.Byte
    private short type;

    @Preset.JtStyle.List
    private List<Item> itemList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class Item {
        // /**
        //  * 位置汇报数据体长度
        //  * <li>0：正常位置批量汇报</li>
        //  * <li>1：盲区补报</li>
        //  */
        // // @Preset.JtStyle.Word
        // private int locationDataLength;

        /**
         * 位置汇报数据体
         */
        // @Preset.JtStyle.Object(lengthExpression = "getLocationDataLength()")
        @Preset.JtStyle.Object(prependLengthFieldType = PrependLengthFieldType.u16)
        private BuiltinMessage0200 locationData;

        // @SuppressWarnings("lombok")
        // public int getLocationDataLength() {
        //     return locationDataLength;
        // }
    }

}
