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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamHandlerAdapterBuilder;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author hylexus
 */
public class TcpXtreamHandlerAdapterBuilder extends AbstractXtreamHandlerAdapterBuilder<TcpXtreamHandlerAdapterBuilder, TcpXtreamNettyHandlerAdapter> {
    public TcpXtreamHandlerAdapterBuilder() {
    }

    public TcpXtreamHandlerAdapterBuilder(ByteBufAllocator allocator) {
        super(allocator);
    }

    public TcpXtreamNettyHandlerAdapter build() {
        final XtreamHandler exceptionHandlingHandler = createRequestHandler();
        return new DefaultTcpXtreamNettyHandlerAdapter(exceptionHandlingHandler, super.byteBufAllocator);
    }

}
