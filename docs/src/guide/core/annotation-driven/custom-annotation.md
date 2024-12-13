---
date: 2024-03-10
icon: at
tag:
  - 自定义
  - 注解
---

# 自定义注解

自定义注解都是通过元注解 `@XtreamField` 的来实现的。

本示例将演示通过 `@XtreamField` 来实现一个自定义的日期注解。

## 注解定义

### 注解声明

```java

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@XtreamField(fieldCodec = MyDateTypeFieldCodec.class, dataType = BeanPropertyMetadata.FiledDataType.basic)
public @interface MyDateType {

    @AliasFor(annotation = XtreamField.class, attribute = "order")
    int order() default -1;

    @AliasFor(annotation = XtreamField.class, attribute = "condition")
    String condition() default "";

}
```

### 自定义编解码器

```java
public class MyDateTypeFieldCodec implements FieldCodec<LocalDate> {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public LocalDate deserialize(DeserializeContext context, ByteBuf input, int length) {
        // yyyyMMdd
        final CharSequence dateString = input.readCharSequence(8, StandardCharsets.UTF_8);
        return LocalDate.parse(dateString, formatter);
    }

    @Override
    public void serialize(SerializeContext context, ByteBuf output, LocalDate value) {
        final String dateString = value.format(formatter);
        output.writeCharSequence(dateString, StandardCharsets.UTF_8);
    }
}
```

## 注解使用

### 实体类声明

```java {11}
@Setter
@Getter
@ToString
public class CustomAnnotationDebugEntity01 {

    // 主版本号 无符号数 1字节
    @Preset.RustStyle.u8
    private short majorVersion = 2;

    // 生日 String[8], "yyyyMMdd", "UTF-8" 8字节
    @MyDateType
    private LocalDate birthday;

    // ... 其他属性 ...
}
```

### 编解码示例

```java {16,13}
public class CustomAnnotationTest {

    final EntityCodec entityCodec = new EntityCodec();

    @Test
    void testEncode() {

        final CustomAnnotationDebugEntity01 originalInstance = new CustomAnnotationDebugEntity01();
        originalInstance.setMajorVersion((short) 2);
        originalInstance.setBirthday(LocalDate.of(2024, 2, 10));

        // 编码/序列化
        final String hexString = encode(originalInstance);

        // 解码/反序列化
        final CustomAnnotationDebugEntity01 decodedInstance = decode(hexString);

        Assertions.assertEquals(originalInstance.getMajorVersion(), decodedInstance.getMajorVersion());
        Assertions.assertEquals(originalInstance.getBirthday(), decodedInstance.getBirthday());
    }

    private CustomAnnotationDebugEntity01 decode(String hexString) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer().writeBytes(XtreamBytes.decodeHex(hexString));
        try {
            return entityCodec.decode(CustomAnnotationDebugEntity01.class, buffer);
        } finally {
            buffer.release();
        }
    }

    private String encode(CustomAnnotationDebugEntity01 originalInstance) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            entityCodec.encode(originalInstance, buffer);
            return ByteBufUtil.hexDump(buffer);
        } finally {
            buffer.release();
        }
    }
}
```
