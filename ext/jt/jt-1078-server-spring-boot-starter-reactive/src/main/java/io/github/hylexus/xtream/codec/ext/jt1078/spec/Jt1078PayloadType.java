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

package io.github.hylexus.xtream.codec.ext.jt1078.spec;


import io.github.hylexus.xtream.codec.ext.jt1078.exception.Jt1078MessageDecodeException;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078PayloadType;

public interface Jt1078PayloadType {

    byte value();

    String desc();

    default boolean isAudio() {
        return isAudio(this.value());
    }

    static boolean isAudio(byte payloadType) {
        return payloadType <= 28 && payloadType >= 1;
    }

    default boolean isVideo() {
        return isVideo(this.value());
    }

    static boolean isVideo(byte payloadType) {
        return payloadType <= 101 && payloadType >= 92;
    }

    static Jt1078PayloadType createOrDefault(byte pt) {
        return DefaultJt1078PayloadType.fromByte(pt)
                .orElseThrow(() -> new Jt1078MessageDecodeException("Unknown payLoadType " + pt));
    }

}
