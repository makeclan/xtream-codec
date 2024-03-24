@Setter
@Getter
@ToString
public class JtStyleDebugEntity02ForEncodeNested {

    // 消息头
    @Preset.JtStyle.Object
    private Header header;

    // 消息体
    @Preset.JtStyle.Object
    private Body body;

    // 校验码
    @Preset.JtStyle.Byte
    private byte checkSum;


    @Getter
    @Setter
    @ToString
    public static class Header {
        // byte[0-2) 	消息ID word(16)
        @Preset.JtStyle.Word
        private int msgId;

        // byte[2-4) 	消息体属性 word(16)
        @Preset.JtStyle.Word
        private int msgBodyProps;

        // byte[4]     协议版本号
        @Preset.JtStyle.Byte
        private byte protocolVersion;

        // byte[5-15) 	终端手机号或设备ID bcd[10]
        @Preset.JtStyle.BCD
        private String terminalId;

        // byte[15-17) 	消息流水号 word(16)
        @Preset.JtStyle.Word
        private int msgSerialNo;

        // byte[17-21) 	消息包封装项
        @Preset.JtStyle.Dword
        private Long subPackageInfo;
    }


    @Getter
    @Setter
    @ToString
    public static class Body {
        // 报警标志  DWORD(4)
        @Preset.JtStyle.Dword
        private long alarmFlag;

        // 状态  DWORD(4)
        @Preset.JtStyle.Dword
        private long status;

        // 纬度  DWORD(4)
        @Preset.JtStyle.Dword
        private long latitude;

        // 经度  DWORD(4)
        @Preset.JtStyle.Dword
        private long longitude;

        // 高程  WORD(2)
        @Preset.JtStyle.Word
        private int altitude;

        // 速度  WORD(2)
        @Preset.JtStyle.Word
        private int speed;

        // 方向  WORD(2)
        @Preset.JtStyle.Word
        private int direction;

        // 时间  BCD[6] yyMMddHHmmss
        @Preset.JtStyle.BCD
        private String time;

        @Preset.JtStyle.List
        private List<ExtraItem> extraItems;
    }


    @Setter
    @Getter
    @ToString
    public static class ExtraItem {
        // 附加信息ID   BYTE(1~255)
        @Preset.JtStyle.Byte
        private short id;
        // 附加信息长度   BYTE(1~255)
        @Preset.JtStyle.Byte
        private short contentLength;
        // 附加信息内容  BYTE[N]
        @Preset.JtStyle.Bytes(lengthExpression = "getLength()")
        private byte[] content;

        public ExtraItem() {
        }

        public ExtraItem(short id, short contentLength, byte[] content) {
            this.id = id;
            this.contentLength = contentLength;
            this.content = content;
        }
    }
}
