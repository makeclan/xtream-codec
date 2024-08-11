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

package io.github.hylexus.xtream.codec.ext.jt808.spec;

import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/82">https://github.com/hylexus/jt-framework/issues/82</a>
 */
public interface Jt808MessageEncryptionHandler {

    /**
     * @param header 请求头
     * @param body   请求体；可能是明文也可能是密文，根据 `header` 判断
     * @return 解密之后的明文 或者 原样返回 `body`
     * @see Jt808RequestHeader#messageBodyProps() ()
     * @see Jt808RequestHeader.Jt808MessageBodyProps#encryptionType()
     * @see Jt808RequestHeader.Jt808MessageBodyProps#dataEncryptionType()
     */
    ByteBuf decryptRequestBody(Jt808RequestHeader header, ByteBuf body);

    /**
     * @param describer     本次响应的其他信息
     * @param plaintextBody 明文数据；可能是完整包，也可能是一个子包
     * @return 返回密文 或者 原样返回 {@code plaintextBody}
     * @implNote 如果该方法返回的是一个 <strong>新的 ByteBuf</strong>，那么实现类应该负责将入参 {@code plaintextBody} 释放掉
     * @see Jt808ResponseBody#encryptionType()
     * @see Jt808MessageDescriber#encryptionType()
     */
    ByteBuf encryptResponseBody(Jt808MessageDescriber describer, ByteBuf plaintextBody);

    class NoOps implements Jt808MessageEncryptionHandler {

        @Override
        public ByteBuf decryptRequestBody(Jt808RequestHeader header, ByteBuf body) {
            return body;
        }

        @Override
        public ByteBuf encryptResponseBody(Jt808MessageDescriber describer, ByteBuf plaintextBody) {
            return plaintextBody;
        }
    }
}
