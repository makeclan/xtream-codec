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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values;

import java.util.HashMap;
import java.util.Map;

public interface Jt808MessageDescriptor {

    String getDescription(int messageId);

    class Default implements Jt808MessageDescriptor {
        public static final Map<Integer, String> MESSAGE_ID_DESC_MAPPING;

        static {
            MESSAGE_ID_DESC_MAPPING = new HashMap<>();
            MESSAGE_ID_DESC_MAPPING.put(0x0100, "终端心跳");
            MESSAGE_ID_DESC_MAPPING.put(0x0102, "终端注销");
            MESSAGE_ID_DESC_MAPPING.put(0x0200, "定位数据上报");
        }

        @Override
        public String getDescription(int messageId) {
            return MESSAGE_ID_DESC_MAPPING.get(messageId);
        }
    }
}
