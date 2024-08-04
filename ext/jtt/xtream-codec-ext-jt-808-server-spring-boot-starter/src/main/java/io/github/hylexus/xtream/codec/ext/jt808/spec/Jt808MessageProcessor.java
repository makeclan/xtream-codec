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

import io.github.hylexus.xtream.codec.ext.jt808.exception.Jt808MsgEscapeException;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808MessageProcessor {

    /**
     * 转义请求消息
     *
     * @param byteBuf 请求消息, 不包含分隔符 {@link JtProtocolConstant#PACKAGE_DELIMITER 0X7E}
     * @return 转义之后的消息
     */
    ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    /**
     * 转义响应消息
     *
     * @param byteBuf 响应给客户端的消息, 不包含分隔符 {@link JtProtocolConstant#PACKAGE_DELIMITER 0X7E}
     * @return 转义之后的消息
     */
    ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    /**
     * 就是校验码
     *
     * @param byteBuf 请求消息/响应消息
     * @return 检验码
     */
    byte calculateCheckSum(ByteBuf byteBuf);
}
