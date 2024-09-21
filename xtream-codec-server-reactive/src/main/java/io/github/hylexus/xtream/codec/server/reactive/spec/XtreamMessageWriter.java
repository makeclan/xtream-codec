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

package io.github.hylexus.xtream.codec.server.reactive.spec;

import io.github.hylexus.xtream.codec.common.bean.XtreamMethodParameter;
import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.netty.buffer.ByteBufAllocator;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public interface XtreamMessageWriter extends OrderedComponent {
    Map<String, Object> READ_ONLY_EMPTY_CONTEXT = Collections.unmodifiableMap(new HashMap<>());

    boolean canWrite(XtreamMethodParameter methodParameter);

    Mono<Void> write(XtreamMethodParameter methodParameter, XtreamInbound request, XtreamOutbound response, Object data, Map<String, Object> context);

    default Mono<Void> write(XtreamMethodParameter methodParameter, ByteBufAllocator bufferFactory, XtreamInbound request, XtreamOutbound response, Object data) {
        return write(methodParameter, request, response, data, READ_ONLY_EMPTY_CONTEXT);
    }

}
