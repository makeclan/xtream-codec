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

package io.github.hylexus.xtream.codec.ext.jt808.codec.impl;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808UdpDatagramPackageSplitter;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class DefaultJt808UdpDatagramPackageSplitter implements Jt808UdpDatagramPackageSplitter {

    public DefaultJt808UdpDatagramPackageSplitter() {
    }

    /**
     * 按照 {@code 0x7e} 分隔符拆分 ByteBuf
     *
     * @param byteBuf 待拆分的 ByteBuf
     * @return 拆分之后的 ByteBuf 列表
     */
    @Override
    public List<ByteBuf> split(ByteBuf byteBuf) {
        final List<ByteBuf> result = new ArrayList<>();
        final byte delimiter = (byte) 0x7e; // 分隔符
        int fromIndex = byteBuf.readerIndex();
        final int toIndex = byteBuf.writerIndex();

        try {
            while (true) {
                int nextIndex = byteBuf.indexOf(fromIndex, toIndex, delimiter);
                if (nextIndex < 0) {
                    // 如果找不到分隔符，将剩余部分作为一个整体添加到结果列表中
                    if (fromIndex < toIndex) {
                        result.add(byteBuf.retainedSlice(fromIndex, toIndex - fromIndex));
                    }
                    break;
                } else {
                    if (nextIndex > fromIndex) {
                        result.add(byteBuf.retainedSlice(fromIndex, nextIndex - fromIndex));
                    }
                    fromIndex = nextIndex + 1;
                }
            }
        } catch (Exception e) {
            XtreamBytes.releaseBufList(result);
            throw e;
        }

        return result;
    }
}
