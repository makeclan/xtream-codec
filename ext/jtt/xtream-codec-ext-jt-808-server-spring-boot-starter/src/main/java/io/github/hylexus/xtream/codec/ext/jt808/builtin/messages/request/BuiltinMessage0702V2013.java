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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 驾驶员身份信息采集上报
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage0702V2013 {

    /**
     * 状态
     * <li>0x01：从业资格证IC卡插入（驾驶员上班）</li>
     * <li>0x02：从业资格证IC卡拔出（驾驶员下班）</li>
     */
    @Preset.JtStyle.Byte
    private short status;

    /**
     * 插卡/拔卡时间，YY-MM-DD-hh-mm-ss；
     * <p>
     * 以下字段在状态为0x01时才有效并做填充。
     */
    @Preset.JtStyle.BcdDateTime
    private LocalDateTime time;

    /**
     * IC卡读取结果
     * <li>0x00：IC卡读卡成功</li>
     * <li>0x01：读卡失败，原因为卡片密钥认证未通过</li>
     * <li>0x02：读卡失败，原因为卡片已被锁定</li>
     * <li>0x03：读卡失败，原因为卡片被拔出</li>
     * <li>0x04：读卡失败，原因为数据校验错误</li>
     * <p>
     * 以下字段在IC 卡读取结果等于0x00时才有效。
     */
    @Preset.JtStyle.Byte
    private short icCardReadResult;
    /**
     * 驾驶员姓名长度
     */
    @Preset.JtStyle.Byte
    private short driverNameLength;

    /**
     * 驾驶员姓名
     */
    @Preset.JtStyle.Str(lengthExpression = "getDriverNameLength()")
    private String driverName;

    /**
     * 从业资格证编码
     */
    @Preset.JtStyle.Str(length = 20)
    private String professionalLicenseNo;

    /**
     * 发证机构名称长度
     */
    @Preset.JtStyle.Byte
    private short certificateAuthorityNameLength;

    @Preset.JtStyle.Str(lengthExpression = "getCertificateAuthorityNameLength()")
    private String certificateAuthorityName;

    @Preset.JtStyle.BcdDateTime(length = 4, pattern = "yyyyMMdd")
    private LocalDate certificateExpiresDate;

    @SuppressWarnings("lombok")
    public short getDriverNameLength() {
        return driverNameLength;
    }

    @SuppressWarnings("lombok")
    public short getCertificateAuthorityNameLength() {
        return certificateAuthorityNameLength;
    }
}
