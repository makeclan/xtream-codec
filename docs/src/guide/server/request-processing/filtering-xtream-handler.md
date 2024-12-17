---
icon: arrows-spin
article: false
---

# FilteringXtreamHandler

## 介绍

::: tip

- `FilteringXtreamHandler` 借鉴于 `org.springframework.web.server.handler.FilteringWebHandler`。
- `XtreamFilterChain` 借鉴于 `org.springframework.web.server.WebFilterChain`。
- `DefaultXtreamFilterChain` 借鉴于 `org.springframework.web.server.handler.DefaultWebFilterChain`。

:::

从名称可以看出来，`FilteringXtreamHandler` 本质上是一个 `XtreamHandler`，前缀 `Filtering` 表示该组件具有过滤器的功能。

`FilteringXtreamHandler` 将请求委托给了 `XtreamFilterChain`。

`XtreamFilterChain` 内部维护着一个 `XtreamHandler`(实际上是 `DispatcherXtreamHandler`) 和 一堆的 `XtreamFilter`。

![](/img/server/request-processing/filtering-xtream-handler.png)

下面是 `XtreamFilterChain` 的默认实现的属性依赖：

```java
public class DefaultXtreamFilterChain implements XtreamFilterChain {
    private final List<XtreamFilter> allFilters;
    private final XtreamHandler handler; // 实际上是 `DispatcherXtreamHandler`
    private final XtreamFilter currentFilter;
    private final DefaultXtreamFilterChain chain;
}
```

`XtreamFilterChain` 的大体流程如下：

- 挨个执行 `XtreamFilter filter`
    - 如果某个 `filter` 返回 `Mono.empty()`，则流程就此终止
    - 反之，则继续执行下一个 `filter`
- 等所有 `filter` 都执行完之后，将请求处理委托给 `DispatcherXtreamHandler`

