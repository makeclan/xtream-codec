---
article: false
date: 2025-06-29
icon: fa-solid fa-backward
tag:
  - 内置
  - 注解
  - 长度字段
---

# 前置长度字段

## 介绍

所谓的 "前置长度字段" 指的是下面注解属性：

- `@XtreamField.prependLengthFieldType`
- `@RustStyle.xxx.prependLengthFieldType`
- `@JtStyle.xxx.prependLengthFieldType`
- `@XtreamField.prependLengthFieldLength`
- `@RustStyle.xxx.prependLengthFieldLength`
- `@JtStyle.xxx.prependLengthFieldLength`
- 你自定义的注解中的“前置长度字段”属性

## 场景

假设有类似于下面这样瞎编的协议:

```text
id: u32(32位无符号数)
username: string(utf8字符串)
password: string(utf8字符串)
age: u32(32位无符号数)
```

上面只是数据需求。

一般来说，网络传输的时候，你不得不通过某种方式 指定某些字段 (`username` 和 `password`) 的长度。

不然解码方无法知道 `username` 和 `password` 的数据长度(两个连续字符串的分界在哪里？)。

所以，真实的网络数据可能会变成下面这样：

```text
id: u32(32位无符号数)
usernameLen: u8(8位无符号数) # 不得不 指定后续字段(username)的长度
username: string(utf8字符串)
passwordLen: u8(8位无符号数) # 不得不 指定后续字段(password)的长度
password: string(utf8字符串)
age: u32(32位无符号数)
```

这种场景下，看下面两个例子就明白所谓"前置长度字段" 的作用了。

## 示例

### 没有"前置长度字段"

```java
class PrependLengthFieldTest {

    private final EntityCodec entityCodec = EntityCodec.DEFAULT;

    @Test
    void testWithoutPrependLengthField() {
        final TestEntityWithoutPrependLengthField rawEntity = new TestEntityWithoutPrependLengthField()
                .setId(1L)
                .setUsername("张三")
                .setPassword("<PASSWORD>")
                .setAge(1L);

        // 这里 ”不得不“ 手动计算长度
        rawEntity.setUsernameLength((short) rawEntity.getUsername().getBytes(StandardCharsets.UTF_8).length);
        rawEntity.setPasswordLength((short) rawEntity.getPassword().getBytes(StandardCharsets.UTF_8).length);

        ByteBuf encoderBuffer = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf decoderBuffer = null;
        try {
            this.entityCodec.encode(rawEntity, encoderBuffer);
            final String encodedHexString = FormatUtils.toHexString(encoderBuffer);
            decoderBuffer = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, encodedHexString);
            final TestEntityWithoutPrependLengthField decodedEntity = this.entityCodec.decode(TestEntityWithoutPrependLengthField.class, decoderBuffer);
            Assertions.assertNotNull(decodedEntity);
            Assertions.assertEquals(rawEntity.getId(), decodedEntity.getId());
            Assertions.assertEquals(rawEntity.getUsernameLength(), decodedEntity.getUsernameLength());
            Assertions.assertEquals(rawEntity.getUsername(), decodedEntity.getUsername());
            Assertions.assertEquals(rawEntity.getPasswordLength(), decodedEntity.getPasswordLength());
            Assertions.assertEquals(rawEntity.getPassword(), decodedEntity.getPassword());

        } finally {
            encoderBuffer.release();
            if (decoderBuffer != null) {
                decoderBuffer.release();
                Assertions.assertEquals(0, decoderBuffer.refCnt());
            }
            Assertions.assertEquals(0, encoderBuffer.refCnt());
        }
    }

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class TestEntityWithoutPrependLengthField {
        @Preset.RustStyle.u32
        private Long id;

        // 先解码出 username 的长度字段
        @Preset.RustStyle.u8
        private Short usernameLength;

        @Preset.RustStyle.str(lengthExpression = "getUsernameLength()")
        private String username;

        // 先解码出 password 的长度字段
        @Preset.RustStyle.u8
        private Short passwordLength;

        @Preset.RustStyle.str(lengthExpression = "getPasswordLength()")
        private String password;

        @Preset.RustStyle.u32
        private Long age;

        public Short getUsernameLength() {
            return usernameLength;
        }

        public Short getPasswordLength() {
            return passwordLength;
        }
    }

}

```

### 有"前置长度字段"

```java
class PrependLengthFieldTest {

    private final EntityCodec entityCodec = EntityCodec.DEFAULT;

    @Test
    void testWithPrependLengthField() {
        final TestEntityWithPrependLengthField rawEntity = new TestEntityWithPrependLengthField()
                .setId(1L)
                .setUsername("张三")
                .setPassword("<PASSWORD>")
                .setAge(0L);

        // 这里 不用 手动计算长度（前置长度字段已经自动处理了）
        // rawEntity.setUsernameLength((short) rawEntity.getUsername().getBytes(StandardCharsets.UTF_8).length);
        // rawEntity.setPasswordLength((short) rawEntity.getPassword().getBytes(StandardCharsets.UTF_8).length);

        ByteBuf encoderBuffer = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf decoderBuffer = null;
        try {
            this.entityCodec.encode(rawEntity, encoderBuffer);
            final String encodedHexString = FormatUtils.toHexString(encoderBuffer);
            decoderBuffer = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, encodedHexString);
            final TestEntityWithPrependLengthField decodedEntity = this.entityCodec.decode(TestEntityWithPrependLengthField.class, decoderBuffer);
            Assertions.assertNotNull(decodedEntity);
            Assertions.assertEquals(rawEntity.getId(), decodedEntity.getId());
            Assertions.assertEquals(rawEntity.getUsername(), decodedEntity.getUsername());
            Assertions.assertEquals(rawEntity.getPassword(), decodedEntity.getPassword());
            Assertions.assertEquals(rawEntity.getAge(), decodedEntity.getAge());
        } finally {
            encoderBuffer.release();
            Assertions.assertEquals(0, encoderBuffer.refCnt());
            if (decoderBuffer != null) {
                decoderBuffer.release();
                Assertions.assertEquals(0, decoderBuffer.refCnt());
            }
        }
    }


    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class TestEntityWithPrependLengthField {
        @Preset.RustStyle.u32
        private Long id;

        // username 的长度字段被 prependLengthFieldType = PrependLengthFieldType.u8 自动处理了
        @Preset.RustStyle.str(prependLengthFieldType = PrependLengthFieldType.u8)
        private String username;

        // password 的长度字段被 prependLengthFieldType = PrependLengthFieldType.u8 自动处理了
        @Preset.RustStyle.str(prependLengthFieldType = PrependLengthFieldType.u8)
        private String password;

        @Preset.RustStyle.u32
        private Long age;
    }
}

```
