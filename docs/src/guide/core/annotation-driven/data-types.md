---
date: 2024-03-10
icon: info
---

# 数据类型

## 1.整数类型

| 长度 | 符号位 | JavaType      | Rust风格注解                | JT风格注解                  |
|----|-----|---------------|-------------------------|-------------------------|
| 1  | 无   | `byte/Byte`   | `@Preset.RustStyle.u8`  | `@Preset.JtStyle.Byte`  |
| 1  | _有_ | `short/Short` | `@Preset.RustStyle.i8`  | --                      |
| 2  | 无   | `short/Short` | `@Preset.RustStyle.u16` | `@Preset.JtStyle.WORD`  |
| 2  | _有_ | `int/Integer` | `@Preset.RustStyle.i16` | --                      |
| 4  | 无   | `int/Integer` | `@Preset.RustStyle.u32` | `@Preset.JtStyle.DWORD` |
| 4  | _有_ | `long/Long`   | `@Preset.RustStyle.i32` | --                      |
| 8  | 无   | `long/Long`   | `@Preset.RustStyle.u64` | --                      |

## 2.字符串类型

### 字符串长度

上面提到的整数类型，其大小(字节数)数是固定的。但是对字符串而言，必须手动指定其长度。

底层注解 [@XtreamField](xtream-field-annotation.md) 提供了下面两个属性来指定字符串的长度：

- `int length()` : 指定固定的长度
- `String lengthExpression()` : 通过 `SpEL` 表达式动态计算长度

### 字符串编码

- 编码: `UTF-8`
    - **Rust 风格 注解**: `@Preset.RustStyle.str(length = 8)`
    - **JT 风格注解**: `@Preset.JtStyle.Str(length = 8, charset = "GBK")`
- 编码: `GBK`
    - **Rust 风格 注解**: `@Preset.RustStyle.str(length = 8, charset = "GBK")`
    - **JT 风格注解**: `@Preset.JtStyle.Str(length = 8)`
- 编码: `BCD_8421`
    - **Rust 风格 注解**: `@Preset.RustStyle.str(charset = "bcd_8421", length = 8)`
    - **JT 风格注解**: `@Preset.JtStyle.Bcd(length = 8)`

更多使用示例，请参考 [示例](../samples/custom-protocol-sample-01/flatten-style-demo.md)

## 3.嵌套类型

## 4.List类型

## 5.浮点数
