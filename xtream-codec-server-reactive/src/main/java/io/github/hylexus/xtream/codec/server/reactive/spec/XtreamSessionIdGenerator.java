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

package io.github.hylexus.xtream.codec.server.reactive.spec;


import reactor.netty.Connection;

import java.util.function.Consumer;

/**
 * @author hylexus
 */
public interface XtreamSessionIdGenerator {

    String generateSessionId(XtreamExchange exchange);

    class DefalutXtreamSessionIdGenerator implements XtreamSessionIdGenerator {

        @Override
        public String generateSessionId(XtreamExchange exchange) {
            final Holder holder = new Holder();
            exchange.request().underlyingInbound().withConnection(holder);
            return holder.id;
        }
    }

    class Holder implements Consumer<Connection> {
        private String id;

        @Override
        public void accept(Connection connection) {
            if (this.id == null) {
                this.id = connection.channel().id().asLongText();
            }
        }
    }
}
