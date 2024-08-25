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

import io.github.hylexus.xtream.codec.core.type.Preset;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 终端鉴权 0x0102
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage0102V2019 {
    /**
     * byte[0,1)    BYTE    鉴权码长度
     */
    @Preset.JtStyle.Byte
    private short authCodeLength;

    /**
     * byte[1,n)    STRING  鉴权码内容
     */
    @Preset.JtStyle.Str(lengthExpression = "getAuthCodeLength()")
    private String authCode;

    /**
     * byte[n+1,n+1+15)     BYTE[]  IMEI
     */
    @Preset.JtStyle.Str(length = 15)
    private String imei;

    /**
     * byte[n+16,n+16+20)   BYTE[20]    软件版本号
     * <p>
     * 位数不足时后补 `0x00`
     */
    @Preset.JtStyle.Str(length = 20)
    private String softwareVersion;

    /**
     * 其他 getter 方法由 lombok 生成，不再列出
     * <p>
     * 该方法在表达式中用到了，所以这里单独列出来，方便调试
     */
    @SuppressWarnings("lombok")
    public short getAuthCodeLength() {
        return authCodeLength;
    }

    /**
     * @return 去掉可能存在的后补 `0x00` 的字符串
     */
    public String getFormatedSoftwareVersion() {
        return JtProtocolUtils.trimTailing(this.getSoftwareVersion(), (byte) 0x00);
    }

}
