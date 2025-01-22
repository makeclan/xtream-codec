@Setter
@Getter
@ToString
public class RawStyleDebugEntity01Nested {

    // 整个 Header 封装到一个实体类中
    @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.struct)
    private Header header;

    // 消息体长度 无符号数 2字节
    @XtreamField(length = 2)
    private int msgBodyLength;

    // 整个 Body 封装到一个实体类中
    @XtreamField(dataType = BeanPropertyMetadata.FiledDataType.struct)
    private Body body;

    // 下面是 Header 和 Body 实体类的声明
    @Data
    public static class Header {
        // 固定为 0x80901234
        @XtreamField(length = 4)
        private int magicNumber = 0x80901234;

        // 主版本号 无符号数 1字节
        @XtreamField(length = 1)
        private short majorVersion;

        // 次版本号 无符号数 1字节
        @XtreamField(length = 1)
        private short minorVersion;

        // 消息类型 无符号数 2字节
        @XtreamField(length = 2)
        private int msgType;
    }


    @Data
    public static class Body {
        // 下一个字段长度 无符号数 2字节
        @XtreamField(length = 2)
        private int usernameLength;

        // 用户名 String, "UTF-8"
        @XtreamField(charset = "utf-8", lengthExpression = "getUsernameLength()")
        private String username;

        // 下一个字段长度 无符号数 2字节
        @XtreamField(length = 2)
        private int passwordLength;

        // 密码 String, "GBK"
        @Preset.RustStyle.str(charset = XtreamConstants.CHARSET_NAME_GBK, lengthExpression = "getPasswordLength()")
        private String password;

        // 生日 String[8], "yyyyMMdd", "UTF-8"
        @XtreamField(charset = "UTF-8", length = 8)
        private String birthday;

        // 手机号 BCD_8421[6] "GBK"
        @XtreamField(charset = XtreamConstants.CHARSET_NAME_BCD_8421, length = 6)
        private String phoneNumber;

        // 年龄 无符号数 2字节
        @XtreamField(length = 2)
        private int age;

        // 状态 有符号数 2字节
        @XtreamField(length = 2)
        private short status;
    }
}
