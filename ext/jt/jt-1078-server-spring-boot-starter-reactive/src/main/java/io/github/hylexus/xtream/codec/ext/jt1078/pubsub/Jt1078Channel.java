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
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Jt1078Channel {

    Scheduler scheduler();

    ChannelKey key();

    Jt1078TerminalIdConverter terminalIdConverter();

    void publish(Jt1078Request request);

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator);

    default <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, Jt1078SubscriberCreator.builder().sim(sim).channelNumber(channelNumber).timeout(timeout).metadata(Collections.emptyMap()).build());
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        return this.doSubscribe(cls, creator).dataStream();
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, Jt1078SubscriberCreator.builder().sim(sim).channelNumber(channelNumber).timeout(timeout).metadata(Collections.emptyMap()).build())
                .dataStream();
    }

    void unsubscribe(String id, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason);

    default void unsubscribe(String id) {
        this.unsubscribe(id, null);
    }

    void close(Jt1078Subscriber.Jt1078SubscriberCloseException reason);


    long countSubscribers(Predicate<Jt1078SubscriberDescriptor> predicate);

    Stream<Jt1078SubscriberDescriptor> listSubscribers();

    record ChannelKey(String sim, short channelNumber) {
        public static ChannelKey from(Jt1078Request request) {
            return new ChannelKey(request.sim(), request.channelNumber());
        }

        public static ChannelKey of(String sim, short channelNumber) {
            return new ChannelKey(sim, channelNumber);
        }

        public String generateSubscriberId() {
            return prefix(sim, channelNumber) + UUID.randomUUID().toString().replaceAll("-", "");
        }

        public static String prefix(String sim, short channel) {
            return sim + "_" + channel + "_";
        }

    }

}
