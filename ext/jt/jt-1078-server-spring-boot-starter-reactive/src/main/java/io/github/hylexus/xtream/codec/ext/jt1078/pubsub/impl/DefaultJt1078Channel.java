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

package io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl;

import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.*;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.collector.H264ToFlvJt1078ChannelCollector;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TerminalIdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultJt1078Channel implements Jt1078Channel {

    private static final Logger log = LoggerFactory.getLogger(DefaultJt1078Channel.class);
    private final ChannelKey key;
    private final Jt1078TerminalIdConverter terminalIdConverter;
    // <class, collector>
    private final ConcurrentMap<
            Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>,
            Jt1078ChannelCollector<? extends Jt1078Subscription>
            > collectors = new ConcurrentHashMap<>();

    public DefaultJt1078Channel(ChannelKey key, Jt1078TerminalIdConverter terminalIdConverter) {
        this.key = key;
        this.terminalIdConverter = terminalIdConverter;
    }

    @Override
    public ChannelKey key() {
        return this.key;
    }

    @Override
    public Jt1078TerminalIdConverter terminalIdConverter() {
        return this.terminalIdConverter;
    }

    @Override
    public void publish(Jt1078Request request) {
        for (final Jt1078ChannelCollector<? extends Jt1078Subscription> collector : this.collectors.values()) {
            collector.collect(request);
        }
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        creator.sim(this.terminalIdConverter.convert(creator.sim()));
        final Jt1078ChannelCollector<? extends Jt1078Subscription> channelCollector = this.getOrCreate(cls, creator);
        @SuppressWarnings("unchecked") final Jt1078Subscriber<S> subscriber = (Jt1078Subscriber<S>) channelCollector.doSubscribe(creator);
        return subscriber;
    }

    @Override
    public void unsubscribe(String id, Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        this.collectors.forEach((cls, collector) -> {
            // ...
            collector.unsubscribe(id, reason);
        });
    }

    @Override
    public void close(Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        this.collectors.forEach((cls, collector) -> {
            //  ...
            collector.unsubscribe(reason);
        });
    }

    protected <S extends Jt1078Subscription> Jt1078ChannelCollector<? extends Jt1078Subscription> getOrCreate(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        return this.collectors.computeIfAbsent(cls, k -> new H264ToFlvJt1078ChannelCollector(this.key));
    }
}
