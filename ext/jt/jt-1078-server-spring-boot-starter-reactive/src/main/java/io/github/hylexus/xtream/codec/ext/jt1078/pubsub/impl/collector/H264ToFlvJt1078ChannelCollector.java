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

import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.FlvHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.impl.DefaultFlvEncoder;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.*;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.ByteArrayJt1078Subscription;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.DefaultJt1078Subscriber;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.DefaultJt1078SubscriptionType;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078PayloadType;
import io.netty.buffer.ByteBuf;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class H264ToFlvJt1078ChannelCollector implements Jt1078ChannelCollector<ByteArrayJt1078Subscription> {
    private static final Logger log = LoggerFactory.getLogger(H264ToFlvJt1078ChannelCollector.class);
    protected final ConcurrentMap<String, H264ToFlvSubscriber> subscribers = new ConcurrentHashMap<>();
    protected final Jt1078Channel.ChannelKey channelKey;
    protected final DefaultFlvEncoder flvEncoder;
    static final Set<Jt1078PayloadType> SUPPORTED_PAYLOAD_TYPES = Set.of(
            DefaultJt1078PayloadType.H264
    );

    public H264ToFlvJt1078ChannelCollector(Jt1078Channel.ChannelKey channelKey) {
        this.channelKey = channelKey;
        this.flvEncoder = new DefaultFlvEncoder();
    }

    protected boolean isSupported(Jt1078PayloadType payloadType) {
        return SUPPORTED_PAYLOAD_TYPES.contains(payloadType);
    }

    @Override
    public void collect(Jt1078Request request) {
        final Jt1078PayloadType payloadType = request.header().payloadType();
        if (!this.isSupported(payloadType)) {
            this.doErrorLogIfNecessary(request, payloadType);
            return;
        }
        final List<ByteBuf> bufList = flvEncoder.encode(request);
        // 一般来说: bufList 只有一个元素 ==> 外层循环次数只有一次
        for (final ByteBuf byteBuf : bufList) {
            try {
                final ByteArrayJt1078Subscription subscription = Jt1078Subscription.forByteBuf(DefaultJt1078SubscriptionType.FLV, byteBuf);
                // 内层循环的次数取决于订阅者的数量(同一个 SIM 同一个 channel)
                for (final H264ToFlvSubscriber subscriber : this.subscribers.values()) {
                    final FluxSink<ByteArrayJt1078Subscription> sink = subscriber.sink();

                    // 1. flv-header + sps + pps
                    if (!subscriber.isFlvHeaderSent()) {
                        final ByteBuf basicFrame = flvEncoder.getFlvBasicFrame();
                        if (basicFrame != null) {
                            // header
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, FlvHeader.of(true, this.flvEncoder.isHasAudio()).toBytes(true)));
                            // sps+pps
                            sink.next(Jt1078Subscription.forByteBuf(DefaultJt1078SubscriptionType.FLV, basicFrame));
                            subscriber.setFlvHeaderSent(true);
                        }
                    }

                    // 2. lastIFrame
                    if (!subscriber.isLastIFrameSent()) {
                        final ByteBuf lastIFrame = flvEncoder.getLastIFrame();
                        if (lastIFrame != null) {
                            sink.next(Jt1078Subscription.forByteBuf(DefaultJt1078SubscriptionType.FLV, lastIFrame));
                            subscriber.setLastIFrameSent(true);
                        }
                    }

                    // 确保先把 关键帧 发送出去(中途订阅)
                    if (!subscriber.isLastIFrameSent()) {
                        return;
                    }
                    // 确保先把 flvHeader 发送出去(中途订阅)
                    if (!subscriber.isFlvHeaderSent()) {
                        return;
                    }
                    // 3. data
                    sink.next(subscription);
                }
            } finally {
                byteBuf.release();
            }
        }
    }

    @Override
    public Jt1078Subscriber<ByteArrayJt1078Subscription> doSubscribe(Jt1078SubscriberCreator creator) {
        final String uuid = this.channelKey.generateSubscriberId();

        final Flux<ByteArrayJt1078Subscription> dataStream = Flux.<ByteArrayJt1078Subscription>create(fluxSink -> {
            log.info("new subscriber created with id: {}", uuid);
            final H264ToFlvSubscriber subscriber = createSubscriber(uuid, creator, fluxSink);
            synchronized (this.subscribers) {
                this.subscribers.put(uuid, subscriber);
            }
        }).timeout(creator.timeout()).doFinally(signalType -> {
            log.info("Subscriber {} removed", uuid);
            synchronized (this.subscribers) {
                this.subscribers.remove(uuid);
            }
        });
        return new DefaultJt1078Subscriber<>(uuid, dataStream);
    }

    @Override
    public void unsubscribe(String id, @Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        final H264ToFlvSubscriber subscriber = this.subscribers.get(id);
        if (subscriber != null) {
            if (reason == null) {
                subscriber.sink().complete();
            } else {
                subscriber.sink().error(reason);
            }
        }
    }

    @Override
    public void unsubscribe(@Nullable Jt1078Subscriber.Jt1078SubscriberCloseException reason) {
        this.subscribers.values().forEach(internalSubscriber -> {
            try {
                if (reason == null) {
                    internalSubscriber.sink().complete();
                } else {
                    internalSubscriber.sink().error(reason);
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    protected H264ToFlvSubscriber createSubscriber(String uuid, Jt1078SubscriberCreator creator, FluxSink<ByteArrayJt1078Subscription> fluxSink) {
        return new H264ToFlvSubscriber(uuid, creator.sim(), creator.channelNumber(), "H.264 --> FLV", LocalDateTime.now(), creator.metadata(), fluxSink);
    }

    private static final Map<Jt1078PayloadType, Boolean> WARNING_FLAGS = new HashMap<>();

    private void doErrorLogIfNecessary(Jt1078Request request, Jt1078PayloadType payloadType) {
        final Boolean flag = WARNING_FLAGS.get(payloadType);
        if (flag == null || !flag) {
            WARNING_FLAGS.put(payloadType, true);
            log.error("Unsupported payloadType : {}. sim = {}, channelNumber = {}, dataType = {}, request = {}", payloadType, request.sim(), request.channelNumber(), request.header().dataType(), request);
        }
    }
}
