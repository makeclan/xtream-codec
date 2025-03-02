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
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BuiltinMessage0002Test extends BaseCodecTest {

    @Test
    void testEncode() {
        final BuiltinMessage0002 entity = new BuiltinMessage0002();
        final String hex = encode(entity, Jt808ProtocolVersion.VERSION_2019, terminalId2019);

        assertEquals("7e0002400001000000000139123443290000377e", hex);
    }

    @Test
    void testDecode() {
        final String hex = "000240000100000000013912344329000037";
        final BuiltinMessage0002 entity = decodeAsEntity(BuiltinMessage0002.class, hex);

        assertNotNull(entity);
        final Jt808Request jt808Request = decodeAsRequest(hex);
        assertEquals(0x0002, jt808Request.messageId());
        jt808Request.release();
        assertEquals(0, jt808Request.body().refCnt());
    }

}
