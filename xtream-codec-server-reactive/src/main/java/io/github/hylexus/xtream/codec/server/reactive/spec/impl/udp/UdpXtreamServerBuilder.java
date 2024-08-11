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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hylexus
 */
public class UdpXtreamServerBuilder {

    protected final List<UdpNettyServerCustomizer> customizers;

    public UdpXtreamServerBuilder() {
        this.customizers = new ArrayList<>();
    }

    public UdpXtreamServerBuilder addServerCustomizers(Collection<UdpNettyServerCustomizer> customizers) {
        this.customizers.addAll(customizers);
        return this;
    }

    public UdpXtreamServerBuilder addServerCustomizer(UdpNettyServerCustomizer customizer) {
        this.customizers.add(customizer);
        return this;
    }

    public UdpXtreamServer build() {
        return new UdpXtreamServer(this.customizers);
    }
}
