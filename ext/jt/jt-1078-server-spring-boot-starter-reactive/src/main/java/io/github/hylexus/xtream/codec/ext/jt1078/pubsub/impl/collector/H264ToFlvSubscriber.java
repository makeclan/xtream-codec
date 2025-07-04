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

package io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.collector;

import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.ByteArrayJt1078Subscription;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class H264ToFlvSubscriber extends DefaultByteArraySubscriber {

    private boolean flvHeaderSent;
    private boolean lastIFrameSent;

    public H264ToFlvSubscriber(String id, String sim, short channel, String desc, LocalDateTime createdAt, Map<String, Object> metadata, FluxSink<ByteArrayJt1078Subscription> sink) {
        super(id, sim, channel, desc, createdAt, metadata, sink);
    }
}
