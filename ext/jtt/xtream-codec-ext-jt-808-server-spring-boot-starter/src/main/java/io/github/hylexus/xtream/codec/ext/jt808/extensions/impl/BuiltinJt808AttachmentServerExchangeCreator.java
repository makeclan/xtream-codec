package io.github.hylexus.xtream.codec.ext.jt808.extensions.impl;

import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808AttachmentServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808AttachmentSessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamExchangeCreator;

public class BuiltinJt808AttachmentServerExchangeCreator
        extends DefaultXtreamExchangeCreator
        implements Jt808AttachmentServerExchangeCreator {

    public BuiltinJt808AttachmentServerExchangeCreator(Jt808AttachmentSessionManager sessionManager) {
        super(sessionManager);
    }
}
