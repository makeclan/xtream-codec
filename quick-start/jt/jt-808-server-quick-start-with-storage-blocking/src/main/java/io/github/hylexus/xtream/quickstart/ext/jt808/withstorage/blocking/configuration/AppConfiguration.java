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

package io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration;

import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageEncryptionHandler;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database.DemoDatabaseAutoConfiguration;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.oss.DemoOssAutoConfiguration;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.props.QuickStartAppProps;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.pubsub.Jt808TraceLogSubscriber;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.service.AttachmentInfoService;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.service.ObjectStorageService;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.service.TraceLogService;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.service.impl.composite.AttachmentInfoServiceComposite;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.service.impl.composite.ObjectStorageServiceComposite;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.service.impl.composite.TraceLogServiceComposite;
import io.netty.buffer.ByteBuf;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author hylexus
 */
@Configuration
@Import({
        DemoDatabaseAutoConfiguration.class,
        DemoOssAutoConfiguration.class,
})
@EnableConfigurationProperties({
        QuickStartAppProps.class,
})
public class AppConfiguration {

    @Bean
    @Primary
    TraceLogService traceLogService(List<TraceLogService> traceLogServiceList) {
        return new TraceLogServiceComposite(traceLogServiceList);
    }

    @Bean
    @Primary
    AttachmentInfoService attachmentInfoService(List<AttachmentInfoService> attachmentInfoServices) {
        return new AttachmentInfoServiceComposite(attachmentInfoServices);
    }

    @Bean
    @Primary
    ObjectStorageService objectStorageService(List<ObjectStorageService> objectStorageServices) {
        return new ObjectStorageServiceComposite(objectStorageServices);
    }

    @Bean
    public CommandLineRunner jt808TraceLogSubscriberCommandLineRunner(Jt808TraceLogSubscriber subscriber) {
        return args -> {
            // traceLog 订阅
            subscriber.subscribe();
        };
    }

    @Bean
    Jt808RequestDecoder jt808RequestDecoder(Jt808BytesProcessor jt808BytesProcessor, Jt808MessageEncryptionHandler encryptionHandler, Jt808RequestCombiner requestCombiner) {
        return new DefaultJt808RequestDecoder(jt808BytesProcessor, encryptionHandler, requestCombiner) {
            @Override
            protected Jt808ProtocolVersion detectVersion(int messageId, Jt808RequestHeader.Jt808MessageBodyProps messageBodyProps, ByteBuf byteBuf) {
                final byte version = byteBuf.getByte(4);
                if (version == Jt808ProtocolVersion.VERSION_2019.versionBit()) {
                    return Jt808ProtocolVersion.VERSION_2019;
                }
                if (messageBodyProps.intValue() > 37) {
                    return Jt808ProtocolVersion.VERSION_2013;
                }
                return Jt808ProtocolVersion.VERSION_2011;
            }
        };
    }

}
