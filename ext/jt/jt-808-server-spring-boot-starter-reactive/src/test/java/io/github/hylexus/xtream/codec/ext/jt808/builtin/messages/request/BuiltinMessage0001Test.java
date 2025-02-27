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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request;

import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.BaseCodecTest;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMessage0001Test extends BaseCodecTest {

    @Test
    void testEncode() {
        final BuiltinMessage0001 entity = new BuiltinMessage0001()
                .setServerFlowId(123)
                .setServerMessageId(0x0200)
                .setResult((short) 1);
        final String hex = encode(entity, Jt808ProtocolVersion.VERSION_2019, terminalId2019);

        assertEquals("7e8001400501000000000139123443290000007b020001c97e", hex);
    }

    @Test
    void testDecode() {
        final String hex = "8001400501000000000139123443290000007b020001c9";
        final BuiltinMessage0001 entity = decodeAsEntity(BuiltinMessage0001.class, hex);
        assertEquals(123, entity.getServerFlowId());
        assertEquals(0x0200, entity.getServerMessageId());
        assertEquals(1, entity.getResult());
    }

}
