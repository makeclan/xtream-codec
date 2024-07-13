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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.server.reactive.spec.impl.tcp.TcpXtreamServerBuilder;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.udp.UdpXtreamServerBuilder;

/**
 * @author hylexus
 */
public final class XtreamServerBuilder {

    private XtreamServerBuilder() {
    }

    public static UdpXtreamServerBuilder newUdpServerBuilder() {
        return new UdpXtreamServerBuilder();
    }

    public static TcpXtreamServerBuilder newTcpServerBuilder() {
        return new TcpXtreamServerBuilder();
    }

}
