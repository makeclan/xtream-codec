/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.ext.jt808.extensions.handler;


import io.github.hylexus.xtream.codec.core.annotation.XtreamResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageEncryptionType;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant;

import java.lang.annotation.*;


/**
 * @author hylexus
 */
// todo 支持在实体类上标记?
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@XtreamResponseBody
public @interface Jt808ResponseBody {
    /**
     * @return 服务端响应消息的消息类型
     */
    int messageId();

    /**
     * @return 消息体加密类型
     */
    byte encryptionType() default Jt808MessageEncryptionType.DEFAULT_ENCRYPTION_TYPE;

    /**
     * @return 单个消息包的最大字节数, 超过该值会自动分包发送
     */
    int maxPackageSize() default JtProtocolConstant.DEFAULT_MAX_PACKAGE_SIZE;

    /**
     * @return 消息头的消息体属性字段中保留的第15个二进制位
     */
    byte reversedBit15InHeader() default 0;

    String desc() default "";
}
