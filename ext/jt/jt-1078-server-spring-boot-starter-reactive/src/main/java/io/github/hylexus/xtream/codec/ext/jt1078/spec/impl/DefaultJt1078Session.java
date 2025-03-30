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

import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionEventListener;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamOutbound;
import io.netty.buffer.ByteBufAllocator;
import reactor.netty.NettyOutbound;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class DefaultJt1078Session extends AbstractXtreamOutbound implements Jt1078Session {
    private final XtreamSessionManager<Jt1078Session> sessionManager;
    private final String id;
    private volatile boolean verified;
    private final Instant creationTime;
    private volatile Instant lastCommunicateTime;
    protected final Map<String, Object> attributes = new HashMap<>();
    private final String terminalId;
    private final short channelNumber;

    public DefaultJt1078Session(String id, ByteBufAllocator byteBufAllocator, NettyOutbound delegate, XtreamRequest.Type type, String terminalId, short channelNumber, InetSocketAddress remoteAddress, XtreamSessionManager<Jt1078Session> sessionManager) {
        super(byteBufAllocator, delegate, type, remoteAddress);
        this.id = id;
        this.sessionManager = sessionManager;
        this.terminalId = terminalId;
        this.channelNumber = channelNumber;
        this.creationTime = this.lastCommunicateTime = Instant.now();
    }

    @Override
    public boolean verified() {
        return this.verified;
    }

    @Override
    public Jt1078Session verified(boolean verified) {
        this.verified = verified;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Map<String, Object> attributes() {
        return this.attributes;
    }

    @Override
    public Instant creationTime() {
        return this.creationTime;
    }

    @Override
    public Instant lastCommunicateTime() {
        return this.lastCommunicateTime;
    }

    @Override
    public Jt1078Session lastCommunicateTime(Instant current) {
        this.lastCommunicateTime = current;
        return this;
    }

    @Override
    public void invalidate(XtreamSessionEventListener.SessionCloseReason reason) {
        this.attributes().clear();
        this.sessionManager.closeSession(this, reason);
    }

    @Override
    public String terminalId() {
        return this.terminalId;
    }

    @Override
    public short channelNumber() {
        return this.channelNumber;
    }

}
