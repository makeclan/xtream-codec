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

package io.github.hylexus.xtream.codec.ext.jt808.boot.configuration;

import io.github.hylexus.xtream.codec.common.utils.BufferFactoryHolder;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.ext.jt808.boot.listener.XtreamExtJt808ServerStartupListener;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
        BuiltinJt808ServerHandlerConfiguration.class,
        BuiltinJt808ServerTcpConfiguration.class,
        BuiltinJt808ServerUdpConfiguration.class,
        BuiltinJt808ProtocolConfiguration.class,
        BuiltinReactorSchedulerConfiguration.class,
})
@EnableConfigurationProperties({
        XtreamJt808ServerProperties.class,
})
public class XtreamExtJt808ServerAutoConfiguration {

    @Bean
    BufferFactoryHolder bufferFactoryHolder() {
        return new BufferFactoryHolder(ByteBufAllocator.DEFAULT);
    }

    @Bean
    @ConditionalOnMissingBean
    EntityCodec entityCodec() {
        return EntityCodec.DEFAULT;
    }

    @Bean
    XtreamExtJt808ServerStartupListener xtreamExtJt808ServerStartupListener(XtreamJt808ServerProperties serverProps) {
        return new XtreamExtJt808ServerStartupListener(serverProps);
    }

}
