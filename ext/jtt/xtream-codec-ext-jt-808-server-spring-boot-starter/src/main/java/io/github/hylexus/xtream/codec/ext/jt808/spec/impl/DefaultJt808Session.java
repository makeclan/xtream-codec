package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ServerType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class DefaultJt808Session implements Jt808Session.MutableJt808Session {
    private final String id;
    private final XtreamRequest.Type type;
    private final Jt808ServerType role;
    private final Instant creationTime;
    private volatile Instant lastCommunicateTime;
    private volatile boolean verified;
    protected final Map<String, Object> attributes = new HashMap<>();

    // 下面几个属性只有在请求被解析之后才能确定具体值
    private Jt808ProtocolVersion protocolVersion;
    private String terminalId;

    public DefaultJt808Session(String id, Jt808ServerType role, XtreamRequest.Type type) {
        this.id = id;
        this.type = type;
        this.role = role;
        this.creationTime = this.lastCommunicateTime = Instant.now();
        this.verified = false;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public XtreamRequest.Type type() {
        return this.type;
    }

    @Override
    public Jt808ServerType role() {
        return this.role;
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
    public Jt808Session lastCommunicateTime(Instant current) {
        this.lastCommunicateTime = current;
        return this;
    }

    @Override
    public Jt808ProtocolVersion protocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public MutableJt808Session protocolVersion(Jt808ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    @Override
    public String terminalId() {
        return this.terminalId;
    }

    @Override
    public MutableJt808Session terminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    @Override
    public Map<String, Object> attributes() {
        return this.attributes;
    }

    @Override
    public boolean verified() {
        return this.verified;
    }

    @Override
    public Jt808Session verified(boolean verified) {
        this.verified = verified;
        return this;
    }
}
