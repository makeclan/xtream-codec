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

package io.github.hylexus.xtream.codec.server.reactive.spec.resources;

import reactor.netty.resources.LoopResources;
import reactor.netty.transport.Transport;

public sealed interface XtreamNettyResourceFactory
        permits TcpXtreamNettyResourceFactory, UdpXtreamNettyResourceFactory, BaseXtreamNettyResourceFactory {

    LoopResources loopResources();

    /**
     * should prefer running on epoll, kqueue or similar instead of java NIO
     *
     * @see Transport#runOn(LoopResources, boolean)
     */
    boolean preferNative();

    record LoopResourcesProperty(
            String prefix,
            int selectCount,
            int workerCount,
            boolean daemon,
            boolean colocate,
            boolean preferNative) {
    }
}
