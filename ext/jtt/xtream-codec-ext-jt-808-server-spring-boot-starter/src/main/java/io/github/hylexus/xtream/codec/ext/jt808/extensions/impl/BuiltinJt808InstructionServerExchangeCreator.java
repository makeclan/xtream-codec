package io.github.hylexus.xtream.codec.ext.jt808.extensions.impl;

import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808InstructionServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.DefaultXtreamExchangeCreator;

public class BuiltinJt808InstructionServerExchangeCreator extends DefaultXtreamExchangeCreator
        implements Jt808InstructionServerExchangeCreator {

    public BuiltinJt808InstructionServerExchangeCreator(Jt808SessionManager sessionManager) {
        super(sessionManager);
    }
}
