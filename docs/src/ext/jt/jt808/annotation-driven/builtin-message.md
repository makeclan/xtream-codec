---
icon: bell
article: false
---

# 内置消息说明

`jt-808-server-spring-boot-starter-reactive` 中内置了一些 **JT/T 808** 消息的实体类。

这些内置消息都在 `io.github.hylexus.xtream.codec.ext.jt808.builtin.messages` 包下。

::: danger

不建议你直接使用这些内置实体类。你应该复制一份到你项目里。

为什么？

- 这些内置实体类仅仅起个示例的作用
- 字段命名不怎么规范，不同版本可能会有差异
- 不同版本中，内置消息的数据类型也可能会调整

:::
