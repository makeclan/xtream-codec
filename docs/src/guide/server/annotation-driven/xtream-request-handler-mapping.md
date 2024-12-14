---
---

# @XtreamRequestHandlerMapping

## 请先读我

::: danger 仅仅自定义一个注解是没啥用的

**Xtream** 作为一个 **TCP/UDP** 处理的抽象，并不知道你的请求报文格式。

需要你根据自己的报文格式自定义注解，并且扫描被注解标记的方法，并将其和 `XtreamHandlerMapping` 关联起来，才能基于注解编程。

不过，**Xtream** 内置了 `AbstractSimpleXtreamRequestMappingHandlerMapping` 抽象类来支持注解扫描。

:::

## 介绍

`@XtreamRequestHandlerMapping` 的设计初衷是用来标记某个 **java方法**，被标记的方法就是一个 **请求处理器**。
并且该注解只能标记在 **方法** 或其他 **注解** 上。

`@XtreamRequestHandlerMapping` 是一个内置的 <span style="color:red;font-weight:bold;">元注解</span>，一般不会直接使用，而是通过将其标记在你自定义的注解上来使用；
直接使用也可以，只要你能将请求的处理路由到被 `@XtreamRequestHandlerMapping` 标记的处理器方法上就行；但是该注解仅仅定义了一个 `scheduler` 属性，应该没法满足具体场景。

下面是注解定义：

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XtreamRequestHandlerMapping {

    /**
     * 当前处理器方法使用的 {@link reactor.core.scheduler.Scheduler Scheduler} 。
     * <p>
     * 默认逻辑:
     * <ol>
     *     <li>如果用户指定了自定义的调度器名称 {@code scheduler}，就使用 {@link XtreamSchedulerRegistry#getScheduler(String scheduler)} 返回的调度器；
     *     用户未指定自定义调度器时通过后面步骤确定当前处理器方法使用的调度器:</li>
     *     <li>如果当前处理器方法是 <strong style="color:red;">阻塞的</strong>，就使用 {@link XtreamRequestHandler#blockingScheduler()} 或 {@link XtreamSchedulerRegistry#defaultBlockingScheduler()}</li>
     *     <li>如果当前处理器方法是 <strong style="color:red;">非阻塞的</strong>，就使用 {@link XtreamRequestHandler#nonBlockingScheduler()}  或  {@link XtreamSchedulerRegistry#defaultNonBlockingScheduler()} </li>
     * </ol>
     *
     * @apiNote 通过 {@link io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate} 判断处理器方法是否阻塞
     * @see reactor.core.scheduler.Scheduler
     * @see XtreamRequestHandler#nonBlockingScheduler()
     * @see XtreamRequestHandler#blockingScheduler()
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry#getScheduler(String)
     * @see io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamBlockingHandlerMethodPredicate
     */
    String scheduler() default "";
}
```

## XtreamHandlerMethod

上面介绍的 `@XtreamRequestHandlerMapping` 仅仅是个标记，在运行时，你需要将所有被标记的处理器方法的元数据解析出来，可能用到的处理器元数据如下：

- 处理器对应哪个 **java方法**？
- 处理器方法对应的 **请求报文类型**？
- 处理器方法对应的 **响应报文类型**？
- 处理器方法对应的 **参数细节**
    - 有没有参数？
    - 有几个参数？
    - 参数是必须的吗？
    - 参数从哪里来？

内置的 `XtreamHandlerMethod` 就是用来封装这些处理器元数据的。定义如下：

```java
public abstract class XtreamHandlerMethod {
    // 处理器方法所在类
    protected final Class<?> containerClass;
    // 处理器方法所在类的实例(默认是单例)
    protected Object containerInstance;
    // 处理器方法
    protected final Method method;
    // 处理器方法参数
    protected final XtreamMethodParameter[] parameters;
    // 这个处理器被哪个调度器调度(在哪个线程池上运行)
    protected Scheduler scheduler;
    // 调度器名称
    protected String schedulerName;
    // 处理器是否是 非阻塞的
    protected boolean nonBlocking;
    // ...
}
```

具体如何使用，需要结合具体的协议才能确定。 请参考 `Jt808RequestMappingHandlerMapping` 中对 **JT/T 808** 协议的扩展。

## 示例

下面是 **Xtream** 对 **JT/T 808** 的实现中的一个扩展注解，使用 `@XtreamRequestHandlerMapping` 注解标记了具体业务场景中的 `@Jt808RequestHandlerMapping` 注解。

```java
@XtreamRequestHandlerMapping // 作为元注解来标记其他注解
public @interface Jt808RequestHandlerMapping {
    @AliasFor(annotation = XtreamRequestHandlerMapping.class, attribute = "scheduler")
    String scheduler() default "";

    /**
     * @return 当前处理器方法能处理的消息类型
     */
    int[] messageIds();

    /**
     * @return 当前处理器能处理的消息对应的版本
     */
    Jt808ProtocolVersion[] versions() default {Jt808ProtocolVersion.AUTO_DETECTION};

    String desc() default "";
}
```

对该自定义注解的处理逻辑请参考:

- `io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808RequestMappingHandlerMapping`
- `io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMethodHandlerAdapter`
- `io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBodyHandlerResultHandler`

该注解的使用示例如下：

```java
@Component // 和本小节内容无关(基于spring)
@Jt808RequestHandler // 和本小节内容无关(定义了一个筛选器(被@Jt808RequestHandler标记的类中的@Jt808RequestHandlerMapping注解才会被扫描))
public class SomeRequestHandler {

    @Jt808RequestHandlerMapping(messageIds = 0x0102, versions = {Jt808ProtocolVersion.VERSION_2011, Jt808ProtocolVersion.VERSION_2013})
    @Jt808ResponseBody(messageId = 0x8001) // 和本小节内容无关(这个是给 XtreamHandlerResultHandler 用的)
    public Mono<ServerCommonReplyMessage> processMessage0102(Jt808Request request, @Jt808RequestBody BuiltinMessage0102V2013 requestBody) {
        log.info("receive message [0x0100-(v2011 or v2013)]: {}", requestBody);
        final ServerCommonReplyMessage responseBody = ServerCommonReplyMessage.success(request);
        return Mono.just(responseBody);
    }

}
```
