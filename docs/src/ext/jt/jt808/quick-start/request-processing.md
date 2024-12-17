---
icon: file-lines
---

# 请求处理流程

## 请先读我

::: tip 在 `Xtream` 中，如何 **处理** 请求并没有强制的规定

由谁来处理？如何处理？取决于 `XtreamHandlerMapping` 的配置。

具体请求处理流程见： [codec-server-reactive 内置请求处理流程](../../../../guide/server/request-processing/intro.md) 章节。

不过，**Xtream** 为 **JT/T 808 协议** 内置了基于注解的方法级处理器： `@Jt808RequestHandlerMapping`

:::

## 1. 基于内置注解

**Xtream** 对 **JT/T 808** 消息的处理提供了一套注解，详情见 [注解驱动开发](../annotation-driven/overview.md)

## 2. 自定义处理器

你可以扩展自己的请求处理器，详情见 [自定义请求处理器](../customization/request-handler.md)
