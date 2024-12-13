---
date: 2024-03-09
icon: at
author: hylexus
category:
  - 示例
tag:
  - 编码
  - 解码
---

# 嵌套写法示例

::: tip

- 在阅读本文之前，建议先阅读 [协议格式说明](index.md)
- 所谓的 _嵌套写法_ 就是将 `Header` 和 `Body` 分别封装到单独的实体类中。

:::

## 解码

### 实体类定义

::: tabs#demo1

@tab:active Rust 命名风格#rust-style

@[code](@src/core/entity-codec/RustStyleDebugEntity01ForDecodeNested.java)

@tab 原始命名风格#raw-style

@[code](@src/core/entity-codec/RawStyleDebugEntity01ForDecodeNested.java)

@tab JT/T 808 命名风格#jt-style

@[code](@src/core/entity-codec/JtStyleDebugEntity01ForDecodeNested.java)

:::

### 反序列化

```java {12,15}
public class EntityDecodeTest {

    @Test
    void testDecode() {
        final EntityCodec entityCodec = new EntityCodec();

        // buffer 中存储的是要反序列化的数据(这里写死用来演示)
        final String hexString = "8090123401020001003d001678747265616d2d636f6465632ee794a8e688b7e5908d001178747265616d2d636f6465632ec3dcc2eb3230323130323033013911112222270fff9c";
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer().writeBytes(XtreamBytes.decodeHex(hexString));

        try {
            final RustStyleDebugEntity01ForDecodeNested entity = entityCodec.decode(RustStyleDebugEntity01ForDecodeNested.class, buffer);
            System.out.println(entity);
        } finally {
            buffer.release();
        }
    }
}
```

## 编码

### 实体类定义

::: tabs#demo1

@tab:active Rust 命名风格#rust-style

@[code](@src/core/entity-codec/RustStyleDebugEntity01ForEncodeNested.java)

@tab 原始命名风格#raw-style

@[code](@src/core/entity-codec/RawStyleDebugEntity01ForEncodeNested.java)

@tab JT/T 808 命名风格#jt-style

@[code](@src/core/entity-codec/JtStyleDebugEntity01ForEncodeNested.java)

:::

### 序列化

```java {16,20}
public class EntityEncodeTest {

    @Test
    void testEncode() {
        final EntityCodec entityCodec = new EntityCodec();

        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            final RustStyleDebugEntity01ForEncodeNested instance = new RustStyleDebugEntity01ForEncodeNested();
            // 省略属性赋值
            // instance.setXxx(someValue);
            // ...
            instance.setMajorVersion((short) 1);

            // 将 instance 的数据序列化到 buffer 中
            entityCodec.encode(instance, buffer);
            // 使用 buffer
            System.out.println(ByteBufUtil.hexDump(buffer));
        } finally {
            buffer.release();
        }
    }
}
```
