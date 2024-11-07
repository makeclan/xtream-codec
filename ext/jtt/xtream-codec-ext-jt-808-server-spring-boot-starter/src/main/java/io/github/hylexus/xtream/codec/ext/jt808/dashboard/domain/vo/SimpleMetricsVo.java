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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo;

import io.github.hylexus.xtream.codec.ext.jt808.boot.actuator.values.Jt808ServerSimpleMetricsHolder;

public class SimpleMetricsVo extends Jt808ServerSimpleMetricsHolder {
    private EventPublisherMetrics eventPublisher;

    public SimpleMetricsVo(Jt808ServerSimpleMetricsHolder holder, int subscriberCount) {
        super(
                holder.getTcpInstructionSession(),
                holder.getTcpAttachmentSession(),
                holder.getUdpInstructionSession(),
                holder.getUdpAttachmentSession(),
                holder.getTcpInstructionRequest(),
                holder.getTcpAttachmentRequest(),
                holder.getUdpInstructionRequest(),
                holder.getUdpAttachmentRequest()
        );
        this.eventPublisher = new EventPublisherMetrics(new EventSubscriberMetrics(subscriberCount));
    }

    public record EventPublisherMetrics(EventSubscriberMetrics subscriber) {
    }

    public record EventSubscriberMetrics(long total) {
    }

    public EventPublisherMetrics getEventPublisher() {
        return eventPublisher;
    }

    public SimpleMetricsVo setEventPublisher(EventPublisherMetrics eventPublisher) {
        this.eventPublisher = eventPublisher;
        return this;
    }
}
