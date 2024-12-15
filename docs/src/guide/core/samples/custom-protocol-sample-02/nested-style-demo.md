---
date: 2024-03-24
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

@[code](@src/core/entity-codec/demo02/RustStyleDebugEntity02ForDecodeNested.java)

@tab 原始命名风格#raw-style

@[code](@src/core/entity-codec/demo02/RawStyleDebugEntity02ForDecodeNested.java)

@tab JT/T 808 命名风格#jt-style

@[code](@src/core/entity-codec/demo02/JtStyleDebugEntity02ForDecodeNested.java)

:::

### 反序列化

```java {12,15}
public class EntityDecodeTest {

    @Test
    void testDecode() {
        final EntityCodec entityCodec = new EntityCodec();

        // buffer 中存储的是要反序列化的数据(这里写死用来演示)
        final String hexString = "0200402a01000000000139111122220063000000580000006f01dc9a0707456246231d029a005a240322222633010400001a0a02020058030200595b";
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer().writeBytes(XtreamBytes.decodeHex(hexString));

        try {
            final RustStyleDebugEntity02ForDecodeNested entity = entityCodec.decode(RustStyleDebugEntity02ForDecodeNested.class, buffer);
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

@[code](@src/core/entity-codec/demo02/RustStyleDebugEntity02ForEncodeNested.java)

@tab 原始命名风格#raw-style

@[code](@src/core/entity-codec/demo02/RawStyleDebugEntity02ForEncodeNested.java)

@tab JT/T 808 命名风格#jt-style

@[code](@src/core/entity-codec/demo02/JtStyleDebugEntity02ForEncodeNested.java)

:::

### 序列化

```java {16,20}
public class EntityEncodeTest {

    @Test
    void testEncode() {
        final EntityCodec entityCodec = new EntityCodec();

        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            final RustStyleDebugEntity02ForEncodeNested instance = new RustStyleDebugEntity02ForEncodeNested();
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

