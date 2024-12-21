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

import static io.github.hylexus.xtream.codec.common.utils.XtreamBytes.gbkByteCount;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMessage0702V2011Test extends BaseCodecTest {

    @Test
    void testEncode() {
        final BuiltinMessage0702V2011 entity = new BuiltinMessage0702V2011();
        entity.setDriverName("张无忌");
        entity.setDriverIdCardNo("11223344556677889900");
        entity.setProfessionalLicenseNo("11335577992244668800");
        entity.setCertificateAuthorityName("没有名字");

        final String hex = encode(entity, Jt808ProtocolVersion.VERSION_2011, terminalId2011, 0x0702);
        assertEquals("7e07020038013912344321000006d5c5cedebcc93131323233333434353536363737383839393030313133333535373739393232343436363838303008c3bbd3d0c3fbd7d6787e", hex);
    }

    @Test
    void testDecode() {
        final String hex = "07020038013912344321000006d5c5cedebcc93131323233333434353536363737383839393030313133333535373739393232343436363838303008c3bbd3d0c3fbd7d678";
        final BuiltinMessage0702V2011 entity = decodeAsEntity(BuiltinMessage0702V2011.class, hex);
        assertEquals("张无忌", entity.getDriverName());
        assertEquals("11223344556677889900", entity.getDriverIdCardNo());
        assertEquals("11335577992244668800", entity.getProfessionalLicenseNo());
        assertEquals("没有名字", entity.getCertificateAuthorityName());
    }
}
