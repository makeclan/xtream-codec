@Setter
@Getter
@ToString
public class RawStyleDebugEntity02ForEncode {

    // region 消息头
    // byte[0-2) 	消息ID word(16)
    @XtreamField(length = 2)
    private int msgId;

    // byte[2-4) 	消息体属性 word(16)
    @XtreamField(length = 2)
    private int msgBodyProps;

    // byte[4]     协议版本号
    @XtreamField(length = 1)
    private byte protocolVersion;

    // byte[5-15) 	终端手机号或设备ID bcd[10]
    @XtreamField(charset = "bcd_8421")
    private String terminalId;

    // byte[15-17) 	消息流水号 word(16)
    @XtreamField(length = 2)
    private int msgSerialNo;

    // byte[17-21) 	消息包封装项
    @XtreamField(length = 4)
    private Long subPackageInfo;
    // endregion 消息头

    // region 消息体
    // 报警标志  DWORD(4)
    @XtreamField(length = 4)
    private long alarmFlag;

    // 状态  DWORD(4)
    @XtreamField(length = 4)
    private long status;

    // 纬度  DWORD(4)
    @XtreamField(length = 4)
    private long latitude;

    // 经度  DWORD(4)
    @XtreamField(length = 4)
    private long longitude;

    // 高程  WORD(2)
    @XtreamField(length = 2)
    private int altitude;

    // 高程  WORD(2)
    @XtreamField(length = 2)
    private int speed;

    // 方向  WORD(2)
    @XtreamField(length = 2)
    private int direction;

    // 时间  BCD[6] yyMMddHHmmss
    @XtreamField(charset = "bcd_8421")
    private String time;

    @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.sequence)
    private List<ExtraItem> extraItems;
    // endregion 消息体

    // 校验码
    @XtreamField(length = 1)
    private byte checkSum;


    @Setter
    @Getter
    @ToString
    public static class ExtraItem {
        // 附加信息ID   BYTE(1~255)
        @XtreamField(length = 1)
        private short id;
        // 附加信息长度   BYTE(1~255)
        @XtreamField(length = 1)
        private short contentLength;
        // 附加信息内容  BYTE[N]
        @XtreamField(lengthExpression = "getContentLength()")
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
