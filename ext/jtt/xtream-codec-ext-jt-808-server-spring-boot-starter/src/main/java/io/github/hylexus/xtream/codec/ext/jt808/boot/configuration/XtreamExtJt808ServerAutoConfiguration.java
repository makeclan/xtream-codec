/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
