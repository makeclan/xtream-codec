package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration.attachment;

import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808AttachmentServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.impl.BuiltinJt808AttachmentServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808AttachmentSessionManager;
import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808AttachmentSessionManager;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        BuiltinJt808AttachmentServerTcpConfiguration.class,
        BuiltinJt808AttachmentServerUdpConfiguration.class,
})
public class BuiltinJt808AttachmentServerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Jt808AttachmentSessionManager jt808AttachmentSessionManager(XtreamSessionIdGenerator idGenerator) {
        return new DefaultJt808AttachmentSessionManager(idGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808AttachmentServerExchangeCreator jt808AttachmentServerExchangeCreator(Jt808AttachmentSessionManager sessionManager) {
        return new BuiltinJt808AttachmentServerExchangeCreator(sessionManager);
    }
}
