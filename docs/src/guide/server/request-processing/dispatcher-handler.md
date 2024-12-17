---
date: 2024-12-10
icon: arrows-spin
article: false
---

# DispatcherXtreamHandler

## 介绍

::: tip

`DispatcherXtreamHandler` 借鉴于 `org.springframework.web.reactive.DispatcherHandler`。

:::

`DispatcherXtreamHandler` 是 `XtreamHandler` 的内置实现之一。

该实现聚合了 `XtreamHandlerMapping` 、 `XtreamHandlerAdapter` 和 `HandlerResultHandler`。

![](/img/server/request-processing/dispatcher-handler.png)

下面是三个组件的接口定义：

```java
public interface XtreamHandlerMapping extends OrderedComponent {
    // 查找一个能处理当前请求的处理器
    Mono<Object> getHandler(XtreamExchange exchange);
}

public interface XtreamHandlerAdapter extends OrderedComponent {
    // 当前适配器能否适配当前处理器
    boolean supports(Object handler);
    // 处理请求
    Mono<XtreamHandlerResult> handle(XtreamExchange exchange, Object handler);

}

public interface XtreamHandlerResultHandler extends OrderedComponent {
    // 当前处理器能否处理本次请求结果
    boolean supports(XtreamHandlerResult result);
    // 写回数据给客户端(做必要的编码逻辑)
    Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult result);
}
```

`DispatcherXtreamHandler` 的大体流程描述如下：

- 通过 `XtreamHandlerMapping` 查找能处理当前请求的处理器(记为 `Object handler = ...`)
    - 如果找不到能处理当前请求的 `handler`，则抛出 `RequestHandlerNotFoundException` 异常(类似于 **HTTP** 协议中的 **404**)
    - 反之，则继续后续流程
- 查找第一个能适的 `handler` 的 `XtreamHandlerAdapter` 实例(记为 `adapter`)
    - 如果找不到合适的 `adapter`，则抛出 `IllegalStateException` 异常
    - 反之，则继续后续流程
- 调用 `adapter.handle(exchange, handler)` 来处理请求
    - 请求具体如何处理，由具体的 `adapter` 实现来决定
    - 可能是个基于注解的处理器，或者其他类型的处理器
    - 处理结果由 `XtreamHandlerResult` 表示(记为 `handlerResult`)
- 查找第一个能处理 `handlerResult` 的 `XtreamHandlerResultHandler` 实例(记为 `resultHandler`)
    - 如果找不到合适的 `resultHandler`，则抛出 `IllegalStateException` 异常
    - 反之，调用 `resultHandler.handleResult(exchange, handlerResult)` 来处理业务处理结果
        - `handlerResult` 所代表的返回结果是多种多样的，写回客户端的数据最终只能是 `ByteBuf` 类型
        - `Object -> ByteBuf` 的转换由 `resultHandler` 完成

::: warning 关于上述流程中抛出异常的说明

- 找不到 `XtreamHandlerMapping` 时抛出了 `RequestHandlerNotFoundException`
    - 这种情况类似于 **HTTP** 协议中的 **404**，是很正常的现象
    - 所以抛出了一个明确的 `RequestHandlerNotFoundException` 让用户自己处理
- 找不到合适的 `XtreamHandlerAdapter` 或 `XtreamHandlerResultHandler` 时抛出的却是 `IllegalStateException`
    - 这种异常严格意义上来说已经不是 `Exception` 了，应该算是 `Error`
    - 比如，你自定义了某种处理器(`handler`)，但是 `handler` 具体怎么执行的？是由 `XtreamHandlerAdapter` 来决定的，然而你却没有提供对应的 `XtreamHandlerAdapter`。
    - 同理，`XtreamHandlerResultHandler` 抛出 `IllegalStateException` 也是类似的原因

:::

## XtreamHandlerMapping

下面是 **Xtream** 对 **JT/T 808** 的实现中的 `HandlerMapping` 实现:

```java
public class Jt808RequestMappingHandlerMapping extends AbstractXtreamRequestMappingHandlerMapping implements ApplicationContextAware, InitializingBean {
    // <messageId,<version,handler>>
    private final Map<Integer, Map<Jt808ProtocolVersion, XtreamHandlerMethod>> mappings = new HashMap<>();

    // 省略其他细节...

    @Override
    public Mono<Object> getHandler(XtreamExchange exchange) {
        // 请求类型是 Jt808Request
        if (exchange.request() instanceof Jt808Request jt808Request) {
            // 通过消息 ID 查找能处理此消息的 Handler
            final Map<Jt808ProtocolVersion, XtreamHandlerMethod> multiVersionHandlers = mappings.get(jt808Request.header().messageId());
            if (multiVersionHandlers == null || multiVersionHandlers.isEmpty()) {
                return Mono.empty();
            }
            // 再次通过版本号查找
            final XtreamHandlerMethod handler = multiVersionHandlers.get(jt808Request.header().version());
            if (handler == null) {
                return Mono.justOrEmpty(multiVersionHandlers.get(Jt808ProtocolVersion.AUTO_DETECTION));
            }
            return Mono.just(handler);
        }
        // 其他类型的请求，当前组件无法处理 ==> 返回空
        return Mono.empty();
    }

    // 省略其他细节...
}
```

## XtreamHandlerAdapter

下面是内置的对 基于注解的处理器的 `HandlerAdapter` 实现:

```java
public class XtreamHandlerMethodHandlerAdapter implements XtreamHandlerAdapter {

    protected static final Mono<Object[]> EMPTY_ARGS = Mono.just(new Object[0]);
    protected static final Object NO_ARG_VALUE = new Object();
    protected final XtreamHandlerMethodArgumentResolver argumentResolver;
    // 省略其他细节...
    @Override
    public boolean supports(Object handler) {
        // 如果 HandlerMapping 返回的处理器是 XtreamHandlerMethod类型
        // 则当前适配器可以处理
        return handler instanceof XtreamHandlerMethod;
    }

    @Override
    public Mono<XtreamHandlerResult> handle(XtreamExchange exchange, Object handler) {
        final XtreamHandlerMethod handlerMethod = (XtreamHandlerMethod) handler;

        return this.resolveArguments(exchange, handlerMethod)
                // 参数解析结束之后 调度到指定的调度器中执行
                .publishOn(handlerMethod.getScheduler())
                .flatMap(args -> {
                    // ...
                    return handlerMethod.invoke(handlerMethod.getContainerInstance(), args);
                });
    }
    // 省略其他细节...
}
```

## HandlerResultHandler

下面是 **Xtream** 对 **JT/T 808** 的实现中的 `HandlerResultHandler` 实现:

```java
public class Jt808ResponseBodyHandlerResultHandler implements XtreamHandlerResultHandler {
    protected final Jt808ResponseEncoder jt808ResponseEncoder;

    public Jt808ResponseBodyHandlerResultHandler(Jt808ResponseEncoder jt808ResponseEncoder) {
        this.jt808ResponseEncoder = jt808ResponseEncoder;
    }

    @Override
    public boolean supports(XtreamHandlerResult handlerResult) {
        // 处理器被 @Jt808ResponseBody 标记 
        return this.getJt808ResponseBody(handlerResult) != null;
    }

    @Override
    public Mono<Void> handleResult(XtreamExchange exchange, XtreamHandlerResult result) {
        final Jt808ResponseBody annotation = this.getJt808ResponseBody(result);
        final Jt808Request jt808Request = (Jt808Request) exchange.request();
        final Jt808RequestHeader requestHeader = jt808Request.header();

        // 适配方法返回类型 ==> 编码 ==> 发送数据给客户端
        return this.adaptReturnValue(result.getReturnValue()).flatMap(body -> {
            final ByteBuf responseBuf = this.jt808ResponseEncoder.encode(body, requestHeader.version(), requestHeader.terminalId(), annotation);
            return exchange.response().writeWith(Mono.just(responseBuf));
        });
    }
}
```
