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

package io.github.hylexus.xtream.codec.ext.jt1078.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Session;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import io.github.hylexus.xtream.codec.server.reactive.spec.domain.values.UdpSessionIdleStateCheckerProps;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamSessionManager;
import io.netty.buffer.ByteBufAllocator;

public class DefaultJt1078SessionManager
        extends AbstractXtreamSessionManager<Jt1078Session>
        implements Jt1078SessionManager {

    public DefaultJt1078SessionManager(boolean udpSessionIdleStateCheckerEnabled, UdpSessionIdleStateCheckerProps checkerProps, XtreamSessionIdGenerator idGenerator) {
        super(udpSessionIdleStateCheckerEnabled, checkerProps, idGenerator);
    }

    @Override
    protected Jt1078Session doCreateSession(String sessionId, XtreamExchange exchange) {
        final Jt1078Request request = (Jt1078Request) exchange.request();
        final XtreamResponse response = exchange.response();
        return new DefaultJt1078Session(
                sessionId,
                ByteBufAllocator.DEFAULT,
                response.outbound(),
                response.type(),
                request.header().sim(),
                response.remoteAddress(),
                this
        );
    }
}
