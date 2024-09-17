package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ServerType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.AbstractXtreamSessionManager;

public class DefaultJt808SessionManager extends AbstractXtreamSessionManager<Jt808Session>
        implements Jt808SessionManager {

    public DefaultJt808SessionManager(XtreamSessionIdGenerator idGenerator) {
        super(idGenerator);
    }

    @Override
    protected Jt808Session doCreateSession(XtreamExchange exchange) {
        final String sessionId = this.sessionIdGenerator.generateSessionId(exchange);
        return new DefaultJt808Session(sessionId, Jt808ServerType.INSTRUCTION_SERVER, exchange.request().type());
    }
}
