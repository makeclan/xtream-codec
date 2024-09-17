package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMsg9208 {

    // BYTE
    @Preset.JtStyle.Byte
    private short attachmentServerIpLength;

    // STRING
    @Preset.JtStyle.Str
    private String attachmentServerIp;

    // WORD
    @Preset.JtStyle.Word
    private int attachmentServerPortTcp;

    // WORD
    @Preset.JtStyle.Word
    private int attachmentServerPortUdp;

    // BYTE[16]
    @Preset.JtStyle.Object
    private AlarmIdentifier alarmIdentifier;

    // BYTE[32]
    @Preset.JtStyle.Bytes
    private String alarmNo;

    // BYTE[16]
    @Preset.JtStyle.Bytes
    private String reservedByte16 = "0000000000000000";
}
