package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration.instruction;

import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808InstructionServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.impl.BuiltinJt808InstructionServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808SessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        BuiltinJt808InstructionServerTcpConfiguration.class,
        BuiltinJt808InstructionServerUdpConfiguration.class,
})
public class BuiltinJt808InstructionServerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Jt808SessionManager jt808SessionManager(XtreamSessionIdGenerator idGenerator) {
        return new DefaultJt808SessionManager(idGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808InstructionServerExchangeCreator jt808InstructionServerExchangeCreator(Jt808SessionManager sessionManager) {
        return new BuiltinJt808InstructionServerExchangeCreator(sessionManager);
    }
}
