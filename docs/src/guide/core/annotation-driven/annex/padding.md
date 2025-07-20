---
article: false
date: 2025-06-29
icon: fa-solid fa-fill
tag:
  - 内置
  - 注解
  - 字符串填充
---

# 字符串填充

## 介绍 <Badge text="0.0.1-rc.6" type="tip" vertical="top"/>

下面表格内容是 苏标扩展中 表 4-23-报警附件信息消息数据格式的描述。

| 起始字节 | 字段    | 数据长度     | 描述及要求                                                                                              |
|------|-------|----------|----------------------------------------------------------------------------------------------------|
| 0    | 终端ID  | BYTE[7]  | 7个字节，由大写字母和数字组成，此终端ID由制造商自行定义，<Badge text="位数不足时，后补“0x00”" type="tip" vertical="top" color="red"/> |
| 7    | 报警标识号 | BYTE[16] | 报警识别号定义见表4                                                                                         |
| ...  | ...   | ...      | ...                                                                                                |

::: tip

- 注意上表中 **终端ID** 的长度是 7 字节，不够7字节时，会用 `0x00` 补足 7 字节（尾部）。

:::

- 这里介绍的 **字符串填充** 指的就是使用下面几个注解属性来应对这种场景
    - `@XtreamField.paddingLeft()`
    - `@XtreamField.paddingRight()`
    - `@RustStyle.str.paddingRight()`
    - `@RustStyle.str.paddingLeft()`
    - `@JtStyle.Str.paddingLeft()`
    - `@JtStyle.Str.paddingRight()`
    - 你自定义注解中的 `paddingLeft()` 和 `paddingRight()` 属性
- 当然你也可以不使用注解属性，而是手动填充字符串，但是手动填充略显繁琐，还要考虑字符集的问题。

## 示例

```java
class StringFieldPaddingTest {

    EntityCodec entityCodec = EntityCodec.DEFAULT;

    @Test
    void testPaddingUtf8() {
        final TestUtf8StringPaddingEntity rawEntity = new TestUtf8StringPaddingEntity()
                .setId(123L)
                // 15 bytes (UTF8)
                .setStrFiled1("哈哈@1a!😁@")
                // 15 bytes (UTF8)
                .setStrFiled2("呵呵@2a!😁@");
        final ByteBuf encoderBuffer = ByteBufAllocator.DEFAULT.buffer();
        final ByteBuf decoderBuffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            this.entityCodec.encode(rawEntity, encoderBuffer);
            final String hexString = FormatUtils.toHexString(encoderBuffer);
            XtreamBytes.byteBufFromHexString(decoderBuffer, hexString);
            final TestUtf8StringPaddingEntity decodedEntity = this.entityCodec.decode(TestUtf8StringPaddingEntity.class, decoderBuffer);
            assertNotNull(decodedEntity);
            assertEquals(rawEntity.getId(), decodedEntity.getId());
            // 左填充 5 个 "1"(即 0x31，也即十进制的 49)
            assertEquals("1".repeat(5) + rawEntity.getStrFiled1(), decodedEntity.getStrFiled1());
            // 右填充 5 个 "1"(即 0x31，也即十进制的 49)
            assertEquals(rawEntity.getStrFiled2() + "1".repeat(5), decodedEntity.getStrFiled2());
        } finally {
            encoderBuffer.release();
            assertEquals(0, encoderBuffer.refCnt());
            decoderBuffer.release();
            assertEquals(0, decoderBuffer.refCnt());
        }
    }


    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class TestUtf8StringPaddingEntity {

        @Preset.RustStyle.u32(desc = "用户ID(32位无符号数)")
        private Long id;

        @Preset.RustStyle.str(
                prependLengthFieldType = PrependLengthFieldType.u8,
                desc = "strFiled1...",
                // 如果编码后长度不够 20 字节，头部(左边)填充0x31(49)直到长度为20字节
                paddingLeft = @Padding(minEncodedLength = 20, paddingElement = 49)
        )
        private String strFiled1;

        @Preset.RustStyle.str(
                prependLengthFieldType = PrependLengthFieldType.u8,
                desc = "strFiled2...",
                // 如果编码后长度不够 20 字节，尾部(右边)用填充0x31(49)直到长度为20字节
                paddingRight = @Padding(minEncodedLength = 20, paddingElement = 49)
        )
        private String strFiled2;

    }

    @Test
    void testPaddingGbk() {
        final TestGbkStringPaddingEntity rawEntity = new TestGbkStringPaddingEntity()
                .setId(123L)
                // 15 bytes (UTF8)
                .setStrFiled1("哈哈@1a!！@繁體")
                // 15 bytes (UTF8)
                .setStrFiled2("哈哈@1a!！@繁體");
        final ByteBuf encoderBuffer = ByteBufAllocator.DEFAULT.buffer();
        final ByteBuf decoderBuffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            this.entityCodec.encode(rawEntity, encoderBuffer);
            final String hexString = FormatUtils.toHexString(encoderBuffer);
            XtreamBytes.byteBufFromHexString(decoderBuffer, hexString);
            final TestGbkStringPaddingEntity decodedEntity = this.entityCodec.decode(TestGbkStringPaddingEntity.class, decoderBuffer);
            assertNotNull(decodedEntity);
            assertEquals(rawEntity.getId(), decodedEntity.getId());
            // 左填充 5 个 "1"(即 0x31，也即十进制的 49)
            assertEquals("1".repeat(5) + rawEntity.getStrFiled1(), decodedEntity.getStrFiled1());
            // 右填充 5 个 "1"(即 0x31，也即十进制的 49)
            assertEquals(rawEntity.getStrFiled2() + "1".repeat(5), decodedEntity.getStrFiled2());
        } finally {
            encoderBuffer.release();
            assertEquals(0, encoderBuffer.refCnt());
            decoderBuffer.release();
            assertEquals(0, decoderBuffer.refCnt());
        }
    }

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class TestGbkStringPaddingEntity {

        @Preset.RustStyle.u32(desc = "用户ID(32位无符号数)")
        private Long id;

        @Preset.JtStyle.Str(
                prependLengthFieldType = PrependLengthFieldType.u8,
                desc = "strFiled1...",
                // 如果编码后长度不够 20 字节，头部(左边)填充0x31(49)直到长度为20字节
                paddingLeft = @Padding(minEncodedLength = 20, paddingElement = 49)
        )
        private String strFiled1;

        @Preset.JtStyle.Str(
                prependLengthFieldType = PrependLengthFieldType.u8,
                desc = "strFiled2...",
                // 如果编码后长度不够 20 字节，尾部(右边)用填充0x31(49)直到长度为20字节
                paddingRight = @Padding(minEncodedLength = 20, paddingElement = 49)
        )
        private String strFiled2;

    }

}
```
