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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@SuperBuilder
@NoArgsConstructor
public class Jt1078SubscriberCreator implements Jt1078SubscriberCreatorInfo {
    private String sim;
    private short channelNumber;

    @Builder.Default
    private boolean hasAudio = true;

    @Builder.Default
    private boolean hasVideo = true;

    private Duration timeout;
    private String desc;
    private Map<String, Object> metadata;

}
