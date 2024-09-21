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

package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration.instruction;

import io.github.hylexus.xtream.codec.common.utils.BufferFactoryHolder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808CommandSender;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808InstructionServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.impl.BuiltinJt808InstructionServerExchangeCreator;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.impl.DefaultJt808XtreamCommandSender;
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

    @Bean
    @ConditionalOnMissingBean
    Jt808CommandSender jt808CommandSender(BufferFactoryHolder holder, Jt808SessionManager sessionManager, Jt808ResponseEncoder encoder) {
        return new DefaultJt808XtreamCommandSender(holder.getAllocator(), sessionManager, encoder);
    }
}
