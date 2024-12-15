---
icon: headphones
article: false
---

# 监听器

## Jt808RequestLifecycleListener

### 作用

监听请求处理过程中的几个关键节点。

::: details 点击 查看/隐藏 接口定义

```java
/**
 * JT808 请求生命周期监听器。
 * <p>
 * 这个组件的目的仅仅是将 请求处理的关键节点 通知给用户。不是用来修改请求或响应数据的。
 * <p>
 * 如果有修改请求或响应的需求，请使用 {@link io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter} 实现。
 *
 * <h3 style="color:red">注意:</h3>
 * <p>
 * 不要在这里做任何耗时/阻塞的操作。
 * <p>
 * 推荐的处理方式：发送消息给其他组件之后立即返回。
 *
 * @author hylexus
 * @implNote 不要在这里做任何耗时/阻塞的操作
 */
public interface Jt808RequestLifecycleListener {
    /**
     * 原始请求解码为 JTT/808 请求之后回调
     *
     * @param nettyInbound 请求上下文
     * @param rawPayload   原始请求
     * @param request      解码后的 JTT/808 请求
     * @implNote 不要有阻塞操作
     */
    default void afterRequestDecoded(NettyInbound nettyInbound, ByteBuf rawPayload, Jt808Request request) {
    }

    /**
     * 分包合并之后回调
     *
     * @param exchange      请求上下文
     * @param mergedRequest 由子包合并而来的请求
     * @implNote 不要有阻塞操作
     */
    default void afterSubPackageMerged(XtreamExchange exchange, Jt808Request mergedRequest) {
    }

    /**
     * 写数据给客户端之前回调
     *
     * @param request  请求
     * @param response 给客户端回复的数据
     * @implNote 不要有阻塞操作
     */
    default void beforeResponseSend(Jt808Request request, ByteBuf response) {
    }

    /**
     * (主动)下发指令之前回调
     *
     * @param session 会话信息
     * @param command 下发的指令
     * @see io.github.hylexus.xtream.codec.ext.jt808.extensions.Jt808CommandSender
     */
    default void beforeCommandSend(Jt808Session session, ByteBuf command) {
    }

    class NoopJt808RequestLifecycleListener implements Jt808RequestLifecycleListener {
    }
}
```

:::

### 示例

要自定义 `Jt808RequestLifecycleListener`，只需要将 `Jt808RequestLifecycleListener` 类型的 **Bean** 加入到 **spring** 容器中即可。

```java
@Component
public class DemoJt808RequestLifecycleListener implements Jt808RequestLifecycleListener {
    private final XtreamEventPublisher eventPublisher;

    public DemoJt808RequestLifecycleListener(XtreamEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterRequestDecoded(NettyInbound nettyInbound, ByteBuf rawPayload, Jt808Request request) {
        // 请求被解码之后发送事件
        // 发送事件然后立即返回；不要有阻塞的操作
        this.eventPublisher.publishIfNecessary(
                DemoJt808EventType.RECEIVE_PACKAGE,
                () -> {
                    final Jt808RequestHeader header = request.header();
                    return new DemoJt808EventPayloads.ReceiveRequest(
                            request.requestId(),
                            request.traceId(),
                            header.version().shortDesc(),
                            header.messageBodyProps().hasSubPackage(),
                            header.messageId(),
                            FormatUtils.toHexString(rawPayload)
                    );
                }
        );
    }

    @Override
    public void afterSubPackageMerged(XtreamExchange exchange, Jt808Request mergedRequest) {
        // 分包合并之后发送事件
        // 发送事件然后立即返回；不要有阻塞的操作
        this.eventPublisher.publishIfNecessary(
                DemoJt808EventType.MERGE_PACKAGE,
                () -> {
                    final Jt808RequestHeader header = mergedRequest.header();
                    return new DemoJt808EventPayloads.MergeRequest(
                            mergedRequest.requestId(),
                            mergedRequest.traceId(),
                            header.version().shortDesc(),
                            header.messageBodyProps().hasSubPackage(),
                            header.messageId(),
                            FormatUtils.toHexString(mergedRequest.payload())
                    );
                }
        );
    }

    @Override
    public void beforeResponseSend(Jt808Request request, ByteBuf response) {
        this.eventPublisher.publishIfNecessary(
                DemoJt808EventType.SEND_PACKAGE,
                () -> {
                    // ...
                    return new DemoJt808EventPayloads.SendResponse(
                            request.requestId(),
                            request.traceId(),
                            0,
                            FormatUtils.toHexString(response)
                    );
                }
        );
    }
}
```

## XtreamSessionEventListener

### 作用

监听 `XtreamSession` 状态的变化。

::: details 点击 查看/隐藏 接口定义

```java
public interface XtreamSessionEventListener {

    /**
     * {@code Session} 新建之后回调
     *
     * @param session 新创建的 {@code Session}
     * @apiNote 不应该做阻塞或者耗时的操作
     */
    default void afterSessionCreate(XtreamSession session) {
    }

    /**
     * {@code Session} 关闭之前回调
     *
     * @param session 准备关闭的 {@code Session}
     * @param reason  关闭原因
     * @apiNote 不应该做阻塞或者耗时的操作
     */
    default void beforeSessionClose(XtreamSession session, SessionCloseReason reason) {
    }

    interface SessionCloseReason {
        String getReason();
    }

    enum DefaultSessionCloseReason implements SessionCloseReason {
        EXPIRED("EXPIRED"),
        CLOSED_BY_USER("CLOSED_BY_USER"),
        CLOSED_BY_CLIENT("CLOSED_BY_CLIENT"),
        ;
        private final String reason;

        DefaultSessionCloseReason(String reason) {
            this.reason = reason;
        }

        @Override
        public String getReason() {
            return this.reason;
        }
    }
}
```

:::

### 示例

要自定义 `XtreamSessionEventListener`，只需要将 `XtreamSessionEventListener` 类型的 **Bean** 加入到 **spring** 容器中即可。

```java
@Component
public class DemoXtreamSessionEventListener implements XtreamSessionEventListener {
    private static final Logger log = LoggerFactory.getLogger(DemoXtreamSessionEventListener.class);

    @Override
    public void afterSessionCreate(XtreamSession session) {
        log.info("Session 创建: {}", session);
    }

    @Override
    public void beforeSessionClose(XtreamSession session, SessionCloseReason reason) {
        log.info("Session 销毁. 原因: {}; Session: {}", reason, session);
    }
}

```
