---
icon: book
article: false
---

# 源码阅读建议

## codec-core

这里介绍的是 [xtream-codec-core](/guide/core/quick-start/quick-start.md) 模块的源码阅读建议。可以从下面几个关键组件入手：

- `EntityEncoder`: 基于 `@XtreamField` 注解的编码器实现
- `EntityDecoder`: 基于 `@XtreamField` 注解的解码器实现
- `EntityCodec`: `EntityEncoder` 和 `EntityDecoder` 的组合
- `FieldCodec`: 对 `@XtreamField` 注解的元数据封装

下面是一些内置的 `FieldCodec` 实现：

![内置 FieldCodec](/img/faq/code-analysis/field-codec.png)

## codec-server-reactive

这里介绍的是 [xtream-codec-server-reactive](/guide/server/quick-start/terminology.md) 模块的源码阅读建议。可以从下面几个关键组件入手：

- `XtreamNettyHandlerAdapter`: 从这里开始接管了 **ReactorNetty** 收到的数据
- `XtreamExchangeCreator`: 封装请求信息
- `XtreamHandler`: 请求处理器
    - `DispatcherXtreamHandler`: 默认 `XtreamHandler` 实现
        - `XtreamHandlerMapping`
        - `XtreamHandlerAdapter`
        - `XtreamHandlerResultHandler`
    - `FilteringXtreamHandler`: 具有 `Filter` 能力的 `XtreamHandler` 实现
        - `XtreamFilterChain`
        - `XtreamFilter`
    - `ExceptionHandlingXtreamHandler`: 具有异常处理能力的 `XtreamHandler` 实现
        - `XtreamRequestExceptionHandler`

## jt-808-server扩展

这里介绍的是 [ext/jt/jt-808-server-spring-boot-starter-reactive](/ext/jt/jt808/quick-start/terminology.md)
模块的源码阅读建议。可以从下面几个关键组件入手：

- `BuiltinJt808AttachmentServerExchangeCreator`: 封装 **JT/T 808** 请求信息
- `Jt808RequestMappingHandlerMapping`: 请求映射(确定请求处理器 `@Jt808RequestHandlerMapping`)
- `Jt808ResponseBodyHandlerResultHandler`: 响应体处理器(`@Jt808ResponseBody`)
- `Jt808RequestLifecycleListener`: 请求生命周期监听器
- `Jt808RequestCombinerFilter`: 分包合并
