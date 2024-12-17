---
icon: arrow-right-arrow-left
article: false
---

# 请求处理流程

## 请先读我

::: tip 在 `Xtream` 中，如何 **处理** 请求并没有强制的规定

请求由谁来处理？如何处理？ 已经内置了一套注解。

:::

一般的请求处理流程大概涉及到下面几个方面：

1. 报文解析
    - 收到的报文是字节流，不方便处理，往往都会将其抽象为一个请求对象。
    - 在 **Xtream** 中，`XtreamRequest` 就是一个请求对象，它封装了原始的请求报文。
    - 在 **JT/T 808** 的实现中，将 `XtreamRequest` 进一步抽象为 [Jt808Request](./terminology.md#jt808request)，新增了几个方法。
2. 请求路由
    - 上面提到的 `XtreamRequest` 到底由哪个处理器来处理呢？
    - 内置了基于 `@Jt808RequestHandlerMapping` 注解的处理器
3. 业务处理
    - 请求已经被路由到了具体的处理器，接下来如何处理，就看你的业务逻辑了。不再赘述。
4. 响应(可选)
    - 内置了 `@Jt808ResponseBody` 注解，用于将业务处理结果编码为 **JT/T 808** 格式的报文。
    - 当然也可以不响应，直接返回 `void / Mono.empty()` 即可。
5. 异常处理
    - 业务处理过程中，如果出现了异常，如何处理？
    - 内置了: `XtreamRequestExceptionHandler` 来统一处理异常

对于上述请求处理流程，有如下两种处理方式：

## 1. 基于内置注解

**Xtream** 对 **JT/T 808** 消息的处理提供了一套注解，详情见 [注解驱动开发](../annotation-driven/overview.md)

## 2. 自定义处理器

你可以扩展自己的请求处理器，详情见 [自定义请求处理器](../customization/request-handler.md)
