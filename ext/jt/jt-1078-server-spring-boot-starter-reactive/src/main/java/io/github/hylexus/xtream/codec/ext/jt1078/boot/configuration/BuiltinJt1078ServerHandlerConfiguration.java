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

package io.github.hylexus.xtream.codec.ext.jt1078.boot.configuration;

import io.github.hylexus.xtream.codec.ext.jt1078.boot.condition.ConditionalOnJt1078Server;
import io.github.hylexus.xtream.codec.ext.jt1078.extensions.handler.DefaultJt1078XtreamHandlerMapping;
import io.github.hylexus.xtream.codec.ext.jt1078.extensions.handler.EventBasedJt1078XtreamRequestHandler;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078RequestPublisher;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.DefaultJt1078RequestPublisher;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TerminalIdConverter;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.RemoveLeadingZerosJt1078TerminalIdConverter;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.resources.Jt1078XtreamSchedulerRegistry;
import io.github.hylexus.xtream.codec.server.reactive.spec.impl.EmptyXtreamHandlerResultHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;

@ConditionalOnJt1078Server(protocolType = ConditionalOnJt1078Server.ProtocolType.ANY)
public class BuiltinJt1078ServerHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Jt1078TerminalIdConverter jt1078TerminalIdConverter() {
        return new RemoveLeadingZerosJt1078TerminalIdConverter();
    }

    @Bean
    Jt1078RequestPublisher jt1078RequestPublisher(Jt1078TerminalIdConverter idConverter, Jt1078XtreamSchedulerRegistry schedulerRegistry) {
        final Scheduler scheduler = schedulerRegistry.audioVideoSubscriberScheduler();
        return new DefaultJt1078RequestPublisher(idConverter, scheduler);
    }

    @Bean
    EventBasedJt1078XtreamRequestHandler eventBasedJt1078XtreamRequestHandler(Jt1078RequestPublisher publisher) {
        return new EventBasedJt1078XtreamRequestHandler(publisher);
    }

    @Bean
    DefaultJt1078XtreamHandlerMapping defaultJt1078XtreamHandlerMapping(EventBasedJt1078XtreamRequestHandler handler) {
        return new DefaultJt1078XtreamHandlerMapping(handler);
    }

    @Bean
    @ConditionalOnMissingBean
    EmptyXtreamHandlerResultHandler emptyXtreamHandlerResultHandler() {
        return new EmptyXtreamHandlerResultHandler();
    }

}
