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

package io.github.hylexus.xtream.debug.ext.jt808.codec;


import io.github.hylexus.xtream.codec.common.exception.NotYetImplementedException;
import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageDescriber;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageEncryptionHandler;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtCryptoUtil;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 */
@Component
public class Jt808MsgEncryptionHandlerDemo01 implements Jt808MessageEncryptionHandler {

    public Jt808MsgEncryptionHandlerDemo01() {
    }

    @Override
    public ByteBuf decryptRequestBody(Jt808RequestHeader header, ByteBuf body) {
        final int encryptionType = header.messageBodyProps().encryptionType();
        if (encryptionType == 0) {
            return body;
        }
        // @see https://github.com/hylexus/jt-framework/issues/82
        // 消息属性中的 第10位，11位，12位 为 010 时，表示消息体经过SM4算法加密
        if (encryptionType == 0b010) {
            try {
                return JtCryptoUtil.SM4.ecbDecrypt(getSecretKey(), body);
            } finally {
                XtreamBytes.releaseBuf(body);
            }
        }
        throw new NotYetImplementedException("不支持的加密类型: 0b" + FormatUtils.toBinaryString(encryptionType, 3));
    }

    @Override
    public ByteBuf encryptResponseBody(Jt808MessageDescriber describer, ByteBuf plaintextBody) {
        final int encryptionType = describer.encryptionType();
        if (encryptionType == 0) {
            return plaintextBody;
        }

        // @see https://github.com/hylexus/jt-framework/issues/82
        // 消息属性中的 第10位，11位，12位 为 010 时，表示消息体经过SM4算法加密
        if (encryptionType == 0b010) {
            try {
                return JtCryptoUtil.SM4.ecbEncrypt(getSecretKey(), plaintextBody);
            } finally {
                XtreamBytes.releaseBuf(plaintextBody);
            }
        }
        throw new NotYetImplementedException("不支持的加密类型: 0b" + FormatUtils.toBinaryString(encryptionType, 3));
    }

    private byte[] getSecretKey() {
        // 从其他配置中获取密钥
        return XtreamBytes.decodeHex("8e47374be6b8d114cb47be6a9a128a37");
    }
}
