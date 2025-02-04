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

package io.github.hylexus.xtream.codec.ext.jt808.spec;

import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.NoOpJt808MessageCodecTracker;
import io.netty.buffer.ByteBuf;

public interface Jt808MessageCodecTracker {
    Jt808MessageCodecTracker NO_OP = NoOpJt808MessageCodecTracker.INSTANCE;

    void beforeMessageBodyEncrypt(int totalSubPackageCount, int currentPackageNo, int flowId, Jt808MessageDescriber describer, ByteBuf bodyBeforeEncryption);

    void afterMessageEncoded(int totalSubPackageCount, int currentPackageNo, int flowId, byte checkSum, Jt808MessageDescriber describer, Jt808RequestHeader jt808RequestHeader, ByteBuf rawBytes, ByteBuf escaped);

}
