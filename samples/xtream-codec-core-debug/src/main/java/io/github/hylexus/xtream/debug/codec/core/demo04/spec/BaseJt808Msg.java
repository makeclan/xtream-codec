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

package io.github.hylexus.xtream.debug.codec.core.demo04.spec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseJt808Msg {

    /**
     * 消息头
     */
    private Jt808MsgHeader header;
    /**
     * <h3 style="color:red;">警告！！！</h3>
     * 不要依赖这个属性。
     * <p>
     * 不要依赖这个属性。
     * <p>
     * 不要依赖这个属性。
     * <p>
     * 这个属性是内部使用的：表示每个子包的消息体。
     * <p>
     * 合并之后的完整包的这个属性是 {@code null}。
     *
     * @see SimpleCache
     */
    private ByteBuf internalTemporarySubPackageBodyByteBuf;

    /**
     * 校验码
     */
    private byte checkSum;

    /**
     * 消息体长度
     */
    public int getMsgBodyLength() {
        return msgBodyLength();
    }

    /**
     * 消息体长度
     */
    public int msgBodyLength() {
        return header.msgBodyLength();
    }

    @Override
    public String toString() {
        return "BaseJt808Msg{"
                + "header=" + header
                + ", checkSum=" + checkSum
                + '}';
    }
}
