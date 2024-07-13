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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamNettyHandlerAdapter;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author hylexus
 */
public interface UdpXtreamNettyHandlerAdapter extends XtreamNettyHandlerAdapter {

    static UdpXtreamHandlerAdapterBuilder newDefaultBuilder() {
        return newDefaultBuilder(ByteBufAllocator.DEFAULT);
    }

    static UdpXtreamHandlerAdapterBuilder newDefaultBuilder(ByteBufAllocator allocator) {
        return new UdpXtreamHandlerAdapterBuilder(allocator);
    }

}
