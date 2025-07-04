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
import jakarta.annotation.Nullable;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Jt1078ChannelCollector<S extends Jt1078Subscription> {

    void collect(Jt1078Request request);

    default Jt1078Subscriber<S> doSubscribe(String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(Jt1078SubscriberCreator.builder().sim(sim).channelNumber(channelNumber).timeout(timeout).build());
    }

    Jt1078Subscriber<S> doSubscribe(Jt1078SubscriberCreator creator);

    default void unsubscribe(String id) {
        this.unsubscribe(id, null);
    }

    void unsubscribe(String id, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason);

    void unsubscribe(@Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason);

    long count(Predicate<Jt1078SubscriberDescriptor> predicate);

    Stream<Jt1078SubscriberDescriptor> list();

}
