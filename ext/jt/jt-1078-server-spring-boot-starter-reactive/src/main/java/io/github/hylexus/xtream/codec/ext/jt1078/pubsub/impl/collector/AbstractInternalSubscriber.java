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

import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078Subscription;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.Map;

public class AbstractInternalSubscriber<S extends Jt1078Subscription>
        implements InternalSubscriber<S> {

    protected final String id;

    protected final String sim;

    protected final short channel;

    protected final String desc;
    protected final LocalDateTime createdAt;

    protected final FluxSink<S> sink;

    protected final Map<String,Object> metadata;

    public AbstractInternalSubscriber(String id, String sim, short channel, String desc, LocalDateTime createdAt, FluxSink<S> sink, Map<String, Object> metadata) {
        this.id = id;
        this.sim = sim;
        this.channel = channel;
        this.desc = desc;
        this.createdAt = createdAt;
        this.sink = sink;
        this.metadata = metadata;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String sim() {
        return this.sim;
    }

    @Override
    public short channel() {
        return this.channel;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    @Override
    public LocalDateTime createdAt() {
        return this.createdAt;
    }

    @Override
    public FluxSink<S> sink() {
        return this.sink;
    }

    @Override
    public Map<String, Object> metadata() {
        return this.metadata;
    }
}
