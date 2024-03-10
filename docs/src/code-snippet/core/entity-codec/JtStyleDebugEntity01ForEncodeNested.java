@Setter
@Getter
@ToString
public class JtStyleDebugEntity01ForEncodeNested {

    // 整个 Header 封装到一个实体类中
    @Preset.JtStyle.Object
    private Header header;

    // 消息体长度 无符号数 2字节
    @Preset.JtStyle.Word
    private int msgBodyLength;

    // 整个 Body 封装到一个实体类中
    @Preset.JtStyle.Object
    private Body body;

    // 下面是 Header 和 Body 实体类的声明
    @Data
    public static class Header {
        // 固定为 0x80901234
        @Preset.JtStyle.Dword
        private int magicNumber = 0x80901234;

        // 主版本号 无符号数 1字节
        @Preset.JtStyle.Byte
        private short majorVersion;

        // 次版本号 无符号数 1字节
        @Preset.JtStyle.Byte
        private short minorVersion;

        // 消息类型 无符号数 2字节
        @Preset.JtStyle.Word
        private int msgType;
    }


    @Data
    public static class Body {
        // 下一个字段长度 无符号数 2字节
        @Preset.JtStyle.Word
        private int usernameLength;

        // 用户名 String, "UTF-8"
        @Preset.JtStyle.Str(charset = "UTF-8")
        private String username;

        // 下一个字段长度 无符号数 2字节
        @Preset.JtStyle.Word
        private int passwordLength;

        // 密码 String, "GBK"
        @Preset.JtStyle.Str(charset = "GBK")
        private String password;

        // 生日 String[8], "yyyyMMdd", "UTF-8"
        @Preset.JtStyle.Str(charset = "UTF-8")
        private String birthday;

        // 手机号 BCD_8421[6] "GBK"
        @Preset.JtStyle.Bcd
        private String phoneNumber;

        // 年龄 无符号数 2字节
        @Preset.JtStyle.Word
        private int age;

        // 状态 有符号数 2字节
        @Preset.JtStyle.Word
        private short status;
    }
}
