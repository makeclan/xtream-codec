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
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TerminalIdConverter;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class DefaultJt1078RequestPublisher implements Jt1078RequestPublisher {
    private final Jt1078TerminalIdConverter terminalIdConverter;
    private final ConcurrentMap<Jt1078Channel.ChannelKey, Jt1078Channel> channels = new ConcurrentHashMap<>();

    public DefaultJt1078RequestPublisher(Jt1078TerminalIdConverter jt1078TerminalIdConverter) {
        this.terminalIdConverter = jt1078TerminalIdConverter;
    }

    @Override
    public Jt1078TerminalIdConverter terminalIdConverter() {
        return this.terminalIdConverter;
    }

    @Override
    public Mono<Void> publish(Jt1078Request request) {
        final Jt1078Channel.ChannelKey channelKey = Jt1078Channel.ChannelKey.from(request);
        final Jt1078Channel jt1078Channel = this.channels.computeIfAbsent(channelKey, this::createChannel);
        jt1078Channel.publish(request);
        return Mono.empty();
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        final Jt1078Channel.ChannelKey key = Jt1078Channel.ChannelKey.of(creator.sim(), creator.channelNumber());
        final Jt1078Channel jt1078Channel = this.channels.computeIfAbsent(key, k -> new DefaultJt1078Channel(k, this.terminalIdConverter));
        return jt1078Channel.doSubscribe(cls, creator);
    }

    @Override
    public void unsubscribe(String id, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        for (final Map.Entry<Jt1078Channel.ChannelKey, Jt1078Channel> entry : this.channels.entrySet()) {
            final Jt1078Channel.ChannelKey channelKey = entry.getKey();
            final String prefix = Jt1078Channel.ChannelKey.prefix(channelKey.sim(), channelKey.channelNumber());
            if (id.startsWith(prefix)) {
                final Jt1078Channel jt1078Channel = entry.getValue();
                jt1078Channel.unsubscribe(id);
            }
        }
    }

    @Override
    public void unsubscribeWithSim(String sim, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        this.doUnsubscribe(channelKey -> channelKey.sim().equals(sim), reason);
    }

    @Override
    public void unsubscribeWithSimAndChannelNumber(String sim, short channelNumber, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        this.doUnsubscribe(channelKey -> channelKey.sim().equals(sim) && channelKey.channelNumber() == channelNumber, reason);
    }

    private void doUnsubscribe(Predicate<Jt1078Channel.ChannelKey> predicate, Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        for (final Iterator<Map.Entry<Jt1078Channel.ChannelKey, Jt1078Channel>> iterator = this.channels.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Jt1078Channel.ChannelKey, Jt1078Channel> entry = iterator.next();
            final Jt1078Channel.ChannelKey channelKey = entry.getKey();
            if (predicate.test(channelKey)) {
                iterator.remove();
                final Jt1078Channel jt1078Channel = entry.getValue();
                jt1078Channel.close(reason);
            }
        }
    }

    protected Jt1078Channel createChannel(Jt1078Channel.ChannelKey channelKey) {
        return new DefaultJt1078Channel(channelKey, this.terminalIdConverter);
    }

}
