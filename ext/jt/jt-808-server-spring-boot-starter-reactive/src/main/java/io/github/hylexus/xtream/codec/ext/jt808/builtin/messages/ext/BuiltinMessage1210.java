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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext;

import io.github.hylexus.xtream.codec.core.annotation.PrependLengthFieldType;
import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.AlarmIdentifier;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.xtream.codec.core.annotation.PrependLengthFieldType.u8;

/**
 * 苏标-表-23 报警附件信息消息
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x1210, desc = "苏标-报警附件信息消息(表-23)")
public class BuiltinMessage1210 {

    /**
     * byte[0,7)
     * <p>
     * 7个字节，由大写字母和数字组成，此终端ID由制造商自行定义，位数不足时，后补“0x00”
     */
    @Preset.JtStyle.Str(length = 7, desc = "终端ID(7)")
    private String terminalId;

    /**
     * byte[7,23)
     * <p>
     * 报警识别号定义见表4-16
     */
    @Preset.JtStyle.Object(length = 16, desc = "报警标识号(16)")
    private AlarmIdentifier alarmIdentifier;

    /**
     * byte[23,55)
     * <p>
     * 平台给报警分配的唯一编号
     */
    @Preset.JtStyle.Bytes(length = 32, desc = "报警编号(32)")
    private String alarmNo;

    /**
     * byte[55]
     * <li>0x00：正常报警文件信息</li>
     * <li>0x01：补传报警文件信息</li>
     */
    @Preset.JtStyle.Byte(desc = "信息类型")
    private short messageType;

    /**
     * byte[56]
     * <p>
     * 与报警关联的附件数量
     */
    @Preset.JtStyle.Byte(desc = "附件数量")
    private short attachmentCount;

    // byte[57,...)
    @Preset.JtStyle.List(desc = "附件信息列表")
    private List<AttachmentItem> attachmentItemList;

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class AttachmentItem {

        // prependLengthFieldType: 前置一个 u8 类型的字段 表示当前字段长度
        @Preset.JtStyle.Str(prependLengthFieldType = u8, lengthExpression = "getLength()", desc = "文件名称")
        private String fileName;

        @Preset.JtStyle.Dword(desc = "文件大小")
        private long fileSize;

        // 这个属性不在报文里  由外部赋值
        private short fileType;
        // 这个属性不在报文里  由外部赋值
        private BuiltinMessage1210 group;

        @Override
        public String toString() {
            return "AttachmentItem{"
                   + "fileName='" + fileName + '\''
                   + ", fileSize=" + fileSize
                   + '}';
        }
    }
}
