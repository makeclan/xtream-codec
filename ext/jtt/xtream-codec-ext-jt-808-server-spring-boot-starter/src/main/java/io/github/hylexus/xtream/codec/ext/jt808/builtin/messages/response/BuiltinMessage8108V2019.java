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
 * 下发终端升级包
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Jt808ResponseBody(messageId = 0x8108)
public class BuiltinMessage8108V2019 {
    /**
     * 升级类型
     * <p>
     * 0：终端，12：道路运输证 IC 卡读卡器，52：北斗卫星定位模块
     */
    @Preset.JtStyle.Byte
    private short type;

    /**
     * 制造商 ID
     */
    @Preset.JtStyle.Bytes(length = 11)
    private String manufacturerId;

    /**
     * 版本号长度
     */
    @Preset.JtStyle.Byte
    private short versionLength;

    /**
     * 版本号
     */
    @Preset.JtStyle.Str(lengthExpression = "getVersionLength()")
    private String version;

    /**
     * 升级数据包长度
     */
    @Preset.JtStyle.Dword
    private long dataSize;

    /**
     * 升级数据包
     */
    @Preset.JtStyle.Bytes
    private byte[] data;

    @SuppressWarnings("lombok")
    public short getVersionLength() {
        return versionLength;
    }
}
