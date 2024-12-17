---
icon: at
article: false
---

# 请求消息映射

## @Jt808RequestHandler

### 作用

::: tip

被该注解标记的类应该是被 **Spring** 管理的类。一般来说应该和 **Spring** 的 `@Component` 等注解同时出现。

:::
`@Jt808RequestHandler` 注解的作用类似于 **WebFlux/WebMvc** 中的 `@Controller`。

就是标记一下被该注解修饰的类中存在若干个能处理请求的处理器方法。

### 属性介绍

| 属性                     | 类型       | 作用                         | 取值示例                  |
|------------------------|----------|----------------------------|-----------------------|
| `nonBlockingScheduler` | `String` | 当前类中 **非阻塞类型** 处理器方法的默认调度器 | `my-custom-scheudler` |
| `blockingScheduler`    | `String` | 当前类中 **阻塞类型** 处理器方法的默认调度器  | `my-custom-scheudler` |

::: tip

调度器详情可参考：[XtreamSchedulerRegistry](/guide/server/core-component/scheduler-registry.md)

:::

## @Jt808RequestHandlerMapping

### 作用

该注解和 **WebFlux/WebMvc** 中 `@RequestMapping` 、`@GetMapping` 等注解功能类似。

表明被该注解修饰的方法具有处理请求的能力。

### 属性介绍

| 属性           | 类型                       | 作用             | 取值示例             |
|--------------|--------------------------|----------------|------------------|
| `scheduler`  | `String`                 | 处理器方法在哪个线程池上运行 | `my-scheudler-1` |
| `messageIds` | `int[]`                  | 处理器方法支持的消息ID   | `0x0200`         |
| `versions`   | `Jt808ProtocolVersion[]` | 处理器方法支持的消息版本   | `VERSION_2019`   |
| `desc`       | `String`                 | 描述信息           | `some desc...`   |

::: tip

调度器详情可参考：[XtreamSchedulerRegistry](/guide/server/core-component/scheduler-registry.md)

:::

## @Jt808RequestBody

### 作用

该注解和 **WebFlux/WebMvc** 中 `@RequestBody` 注解功能类似。 可以将 **请求体** 映射到被该注解修饰的实体类(`DTO`)。

### 属性介绍

目前就一个属性，即 `boolean bufferAsSlice() default true;`。

- `bufferAsSlice = true`
    - 使用 `Jt808Request.body().slice()`进行消息映射，而不是直接读取 `ByteBuf`。
    - `readerIndex` 的改变不会影响 `Jt808Request.body()`
- `bufferAsSlice = false`
    - 直接读取 `ByteBuf`
    - `readerIndex` 会直接影响 `Jt808Request.body()`

### 示例

::: tip

实体类中的注解详情可参考：[@XtreamField](/guide/core/annotation-driven/xtream-field-annotation.md)

:::

定义一个接收请求的实体类：

```java
public class BuiltinMessage0100V2019 {
    // 1. [0-2) WORD 省域ID
    @Preset.JtStyle.Word
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @Preset.JtStyle.Word
    private int cityId;
    // 省略其他属性
}
```

将 **请求体** 映射到该实体类：

```java {5}
@Jt808RequestHandlerMapping(messageIds = 0x0100, versions = Jt808ProtocolVersion.VERSION_2019)
@Jt808ResponseBody(messageId = 0x8100)
public Mono<BuiltinMessage8100> processMessage0x0100V2019(
  // 将请求体映射到实体类
  @Jt808RequestBody BuiltinMessage0100V2019 requestBody
) {
    // ...
}
```

## 参数解析器

被 `@Jt808RequestHandlerMapping` 修饰的处理器方法，一般来说都要有方法参数。比如下面代码中的方法参数: `session`、`request` 和 `body`。

```java {4,5,6}
@Jt808RequestHandlerMapping(messageIds = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
@Jt808ResponseBody(messageId = 0x8001)
public Mono<ServerCommonReplyMessage> processMessage0200V2019(
        Jt808Session session,
        Jt808Request request,
        @Jt808RequestBody BuiltinMessage0200 body) {
    return ...;
}
```

这里说的 **参数解析器** 就是用来给处理器方法提供参数解析逻辑的。

有哪些内置的参数解析器？参考 [参数解析器](./argument-resolver.md)
