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

package io.github.hylexus.xtream.codec.ext.jt1078.pubsub;

import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TerminalIdConverter;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;

public interface Jt1078RequestPublisher extends Jt1078SubscriberManager {

    Jt1078TerminalIdConverter terminalIdConverter();

    Mono<Void> publish(Jt1078Request request);

    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, sim, channelNumber, timeout).dataStream();
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        return this.doSubscribe(cls, creator).dataStream();
    }

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator);

    default <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, Jt1078SubscriberCreator.builder().sim(sim).channelNumber(channelNumber).timeout(timeout).metadata(Collections.emptyMap()).build());
    }

    default void unsubscribe(String id) {
        this.unsubscribe(id, null);
    }

    void unsubscribe(String id, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason);

    void unsubscribeWithSim(String sim, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason);

    default void unsubscribeWithSim(String sim) {
        this.unsubscribeWithSim(terminalIdConverter().convert(sim), null);
    }

    void unsubscribeWithSimAndChannelNumber(String sim, short channelNumber, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason);

    default void unsubscribeWithSimAndChannelNumber(String sim, short channelNumber) {
        this.unsubscribeWithSimAndChannelNumber(terminalIdConverter().convert(sim), channelNumber, null);
    }

    @Override
    default void closeSubscriber(String id) {
        this.unsubscribe(id, null);
    }

}
