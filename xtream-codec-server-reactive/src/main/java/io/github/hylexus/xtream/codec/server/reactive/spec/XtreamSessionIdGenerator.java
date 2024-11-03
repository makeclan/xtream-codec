/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.server.reactive.spec;


import io.netty.channel.Channel;
import reactor.netty.Connection;

import java.util.function.Consumer;

/**
 * @author hylexus
 */
public interface XtreamSessionIdGenerator {

    default String generateSessionId(XtreamExchange exchange) {
        final Holder holder = new Holder();
        exchange.request().underlyingInbound().withConnection(holder);
        return generateSessionId(holder.channel);
    }

    String generateSessionId(Channel channel);

    class DefalutXtreamSessionIdGenerator implements XtreamSessionIdGenerator {

        @Override
        public String generateSessionId(Channel channel) {
            return channel.id().asLongText();
        }
    }

    class Holder implements Consumer<Connection> {
        private Channel channel;

        @Override
        public void accept(Connection connection) {
            if (this.channel == null) {
                this.channel = connection.channel();
            }
        }
    }
}
