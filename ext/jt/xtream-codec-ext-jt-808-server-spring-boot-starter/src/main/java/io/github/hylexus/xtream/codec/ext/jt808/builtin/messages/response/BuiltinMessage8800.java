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
 * 多媒体数据上传应答
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8800)
public class BuiltinMessage8800 {
    /**
     * 多媒体ID
     * <p>
     * > 0，如收到全部数据包则没有后续字段
     */
    @Preset.JtStyle.Dword
    private long multimediaId;

    /**
     * 重传包总数
     */
    @Preset.JtStyle.Byte
    private short retransmittedPackageCount;

    /**
     * 重传包ID列表
     * <p>
     * 重传包序号顺序排列，如“包 ID1 包 ID2......包IDn”。
     */
    @Preset.JtStyle.Bytes(lengthExpression = "2 * getRetransmittedPackageCount()")
    private byte[] retransmittedPackageIdList;

    @SuppressWarnings("lombok")
    public short getRetransmittedPackageCount() {
        return retransmittedPackageCount;
    }
}
