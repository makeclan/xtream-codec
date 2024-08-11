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
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808FlowIdGenerator;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageEncryptionHandler;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.impl.DefaultJt808RequestDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class BuiltinJt808ProtocolConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Jt808FlowIdGenerator jt808FlowIdGenerator() {
        return Jt808FlowIdGenerator.DEFAULT;
    }

    @Bean
    Jt808BytesProcessor jt808BytesProcessor(BufferFactoryHolder bufferFactoryHolder) {
        return new DefaultJt808BytesProcessor(bufferFactoryHolder.getAllocator());
    }

    @Bean
    Jt808RequestDecoder jt808RequestDecoder(Jt808BytesProcessor jt808BytesProcessor) {
        return new DefaultJt808RequestDecoder(jt808BytesProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    Jt808MessageEncryptionHandler jt808MessageEncryptionHandler() {
        return new Jt808MessageEncryptionHandler.NoOps();
    }

    @Bean
    @ConditionalOnMissingBean
    DefaultJt808ResponseEncoder jt808ResponseEncoder(
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
}
