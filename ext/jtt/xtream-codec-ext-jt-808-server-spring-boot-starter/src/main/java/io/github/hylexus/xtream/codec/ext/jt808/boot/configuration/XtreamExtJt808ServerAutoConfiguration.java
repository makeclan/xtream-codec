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
import io.github.hylexus.xtream.codec.core.BeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.FieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.XtreamCacheableClassPredicate;
import io.github.hylexus.xtream.codec.core.impl.DefaultFieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.github.hylexus.xtream.codec.ext.jt808.boot.listener.XtreamExtJt808ServerStartupListener;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.codec.*;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808FlowIdGenerator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageEncryptionHandler;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
        BuiltinJt808ServerHandlerConfiguration.class,
        BuiltinJt808ServerSchedulerConfiguration.class,
})
@EnableConfigurationProperties({
        XtreamJt808ServerProperties.class,
})
@ConditionalOnProperty(prefix = "jt808-server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class XtreamExtJt808ServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    BufferFactoryHolder bufferFactoryHolder() {
        return new BufferFactoryHolder(ByteBufAllocator.DEFAULT);
    }

    @Bean
    @ConditionalOnMissingBean
    FieldCodecRegistry fieldCodecRegistry() {
        return new DefaultFieldCodecRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    XtreamCacheableClassPredicate xtreamCacheableClassPredicate() {
        return new XtreamCacheableClassPredicate.Default();
    }

    @Bean
    @ConditionalOnMissingBean
    BeanMetadataRegistry beanMetadataRegistry(FieldCodecRegistry fieldCodecRegistry, XtreamCacheableClassPredicate cacheableClassPredicate) {
        return new SimpleBeanMetadataRegistry(fieldCodecRegistry, cacheableClassPredicate);
    }

    @Bean
    @ConditionalOnMissingBean
    EntityCodec entityCodec(BeanMetadataRegistry registry) {
        return new EntityCodec(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808FlowIdGenerator jt808FlowIdGenerator() {
        return Jt808FlowIdGenerator.DEFAULT;
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808BytesProcessor jt808BytesProcessor(BufferFactoryHolder bufferFactoryHolder) {
        return new DefaultJt808BytesProcessor(bufferFactoryHolder.getAllocator());
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808RequestDecoder jt808RequestDecoder(Jt808BytesProcessor jt808BytesProcessor, Jt808MessageEncryptionHandler encryptionHandler, Jt808RequestCombiner requestCombiner) {
        return new DefaultJt808RequestDecoder(jt808BytesProcessor, encryptionHandler, requestCombiner);
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808RequestCombiner jt808RequestCombiner(BufferFactoryHolder bufferFactoryHolder, XtreamJt808ServerProperties properties) {
        final XtreamJt808ServerProperties.RequestSubPackageStorage subPackageStorage = properties.getRequestSubPackageStorage();
        return new DefaultJt808RequestCombiner(bufferFactoryHolder.getAllocator(), subPackageStorage.getMaximumSize(), subPackageStorage.getTtl());
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808RequestLifecycleListener jt808RequestLifecycleListener() {
        return new Jt808RequestLifecycleListener.NoopJt808RequestLifecycleListener();
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808MessageEncryptionHandler jt808MessageEncryptionHandler() {
        return new Jt808MessageEncryptionHandler.NoOps();
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808ResponseEncoder jt808ResponseEncoder(
            BufferFactoryHolder factoryHolder,
            Jt808FlowIdGenerator flowIdGenerator,
            EntityCodec entityCodec,
            Jt808BytesProcessor jt808BytesProcessor,
            Jt808MessageEncryptionHandler encryptionHandler) {

        return new DefaultJt808ResponseEncoder(
                factoryHolder.getAllocator(),
                flowIdGenerator,
                entityCodec,
                jt808BytesProcessor,
                encryptionHandler
        );
    }

    @Bean
    XtreamExtJt808ServerStartupListener xtreamExtJt808ServerStartupListener(XtreamJt808ServerProperties serverProps) {
        return new XtreamExtJt808ServerStartupListener(serverProps);
    }

}
