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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hylexus
 */
public class TcpXtreamServerBuilder {
    protected final List<TcpNettyServerCustomizer> customizers;

    public TcpXtreamServerBuilder() {
        this.customizers = new ArrayList<>();
    }

    public TcpXtreamServerBuilder addServerCustomizer(TcpNettyServerCustomizer customizer) {
        this.customizers.add(customizer);
        return this;
    }

    public TcpXtreamServerBuilder addServerCustomizers(Collection<TcpNettyServerCustomizer> customizers) {
        this.customizers.addAll(customizers);
        return this;
    }

    public TcpXtreamServer build() {
        return new TcpXtreamServer(this.customizers);
    }
}
