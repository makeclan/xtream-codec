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

package io.github.hylexus.xtream.codec.base.web.utils;

import io.github.hylexus.xtream.codec.base.web.annotation.ClientIp;
import org.junit.jupiter.api.Test;

import static io.github.hylexus.xtream.codec.base.web.utils.XtreamWebUtils.filterClientIp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class XtreamWebUtilsTest {

    @Test
    void testFilterClientIp() {
        assertEquals("1.2.3.4", filterClientIp("1.2.3.4", "1.1.1.1", false, "localhost"));
        assertEquals("1.1.1.1", filterClientIp(null, "1.1.1.1", false, "localhost"));
        assertNull(filterClientIp("127.0.0.1", "1.1.1.1", true, "localhost"));
        assertNull(filterClientIp("127.0.0.1", "1.1.1.1", true, "1.1.1.1"));

        assertEquals("127.0.0.1", filterClientIp("127.0.0.1", "1.1.1.1", false, "127.0.0.1"));
        assertEquals("localhost", filterClientIp("127.0.0.1", "1.1.1.1", false, "localhost"));
        assertEquals("0:0:0:0:0:0:0:1", filterClientIp("127.0.0.1", "1.1.1.1", false, "0:0:0:0:0:0:0:1"));

        assertNull(filterClientIp("127.0.0.1", "1.1.1.1", false, ClientIp.NULL_PLACEHOLDER));
        assertNull(filterClientIp("0:0:0:0:0:0:0:1", "1.1.1.1", false, ClientIp.NULL_PLACEHOLDER));
        assertNull(filterClientIp("localhost", "1.1.1.1", false, ClientIp.NULL_PLACEHOLDER));

        assertNull(filterClientIp("127.0.0.1", "1.1.1.1", true, ClientIp.NULL_PLACEHOLDER));
        assertNull(filterClientIp("0:0:0:0:0:0:0:1", "1.1.1.1", true, ClientIp.NULL_PLACEHOLDER));
        assertNull(filterClientIp("localhost", "1.1.1.1", true, ClientIp.NULL_PLACEHOLDER));
    }

}
