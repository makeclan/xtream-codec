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


import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078SubscriberDescriptor;

import java.time.LocalDateTime;
import java.util.Map;

public class DefaultJt1078SubscriberDescriptor implements Jt1078SubscriberDescriptor {
    private final String id;
    private final String sim;

    private final short channel;
    private final LocalDateTime createdAt;

    private final String desc;
    private final Map<String, Object> metadata;

    public DefaultJt1078SubscriberDescriptor(String id, String sim, short channel, LocalDateTime createdAt, String desc, Map<String, Object> metadata) {
        this.id = id;
        this.sim = sim;
        this.channel = channel;
        this.createdAt = createdAt;
        this.desc = desc;
        this.metadata = metadata;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getSim() {
        return this.sim;
    }

    @Override
    public short getChannel() {
        return this.channel;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String getDesc() {
        if (this.desc == null) {
            return Jt1078SubscriberDescriptor.super.getDesc();
        }
        return this.desc;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
