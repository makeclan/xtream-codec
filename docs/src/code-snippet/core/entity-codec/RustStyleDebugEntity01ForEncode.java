@Setter
@Getter
@ToString
public class RustStyleDebugEntity01ForEncode {
    //////////////////////// header
    // 固定为 0x80901234
    @Preset.RustStyle.i32
    private int magicNumber = 0x80901234;

    // 主版本号 无符号数 1字节
    @Preset.RustStyle.u8
    private short majorVersion;
    // 次版本号 无符号数 1字节

    @Preset.RustStyle.u8
    private short minorVersion;

    // 消息类型 无符号数 2字节
    @Preset.RustStyle.u16
    private int msgType;

    // 消息体长度 无符号数 2字节
    @Preset.RustStyle.u16
    private int msgBodyLength;

    //////////////////////// body
    // 下一个字段长度 无符号数 2字节
    @Preset.RustStyle.u16
    private int usernameLength;

    // 用户名 String, "UTF-8"
    @Preset.RustStyle.str
    private String username;

    // 下一个字段长度 无符号数 2字节
    @Preset.RustStyle.u16
    private int passwordLength;

    // 密码 String, "GBK"
    @Preset.RustStyle.str(charset = "GBK")
    private String password;

    // 生日 String[8], "yyyyMMdd", "UTF-8"
    @Preset.RustStyle.str
    private String birthday;

    // 手机号 BCD_8421[6] "GBK"
    @Preset.RustStyle.str(charset = "bcd_8421")
    private String phoneNumber;

    // 年龄 无符号数 2字节
    @Preset.RustStyle.u16
    private int age;

    // 状态 有符号数 2字节
    @Preset.RustStyle.i16
    private short status;
}
