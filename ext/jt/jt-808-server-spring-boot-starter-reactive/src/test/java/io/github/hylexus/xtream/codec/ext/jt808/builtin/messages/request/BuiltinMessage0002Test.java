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
