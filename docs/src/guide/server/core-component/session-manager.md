---
date: 2024-12-10
icon: info
---

# 会话管理

::: tip

`XtreamSessionManager` 借鉴自 `org.springframework.web.server.session.WebSessionManager`。

:::

## XtreamSessionManager

顾名思义，`XtreamSessionManager` 就是用来管理 `XtreamSession` 的。 包括会话的创建、销毁、事件监听等。

TCP 和 UDP 都在 `XtreamSessionManager` 中统一管理。

接口定义如下：

```java
public interface XtreamSessionManager<S extends XtreamSession> {

    XtreamSessionIdGenerator sessionIdGenerator();

    default Mono<S> getSession(XtreamExchange exchange, boolean createNewIfMissing) {
        return Mono.defer(() -> {
            if (createNewIfMissing) {
                final String sessionId = this.sessionIdGenerator().generateSessionId(exchange);
                return this.getSessionById(sessionId).switchIfEmpty(Mono.defer(() -> {
                    // ...
                    return this.createSession(sessionId, exchange);
                }));
            }
            return this.getSession(exchange);
        });
    }

    Mono<S> getSession(XtreamExchange exchange);

    Mono<S> createSession(String sessionId, XtreamExchange exchange);

    default Mono<S> createSession(XtreamExchange exchange) {
        final String sessionId = this.sessionIdGenerator().generateSessionId(exchange);
        return this.createSession(sessionId, exchange);
    }

    Mono<S> getSessionById(String sessionId);

    boolean closeSessionById(String sessionId, XtreamSessionEventListener.SessionCloseReason reason);

    /**
     * @see XtreamSession#invalidate(XtreamSessionEventListener.SessionCloseReason)
     */
    void closeSession(S session, XtreamSessionEventListener.SessionCloseReason reason);

    void addListener(XtreamSessionEventListener listener);

    void shutdown();

    long count();

    default long count(Predicate<S> filter) {
        return this.list().filter(filter).count();
    }

    long countTcp();

    long countUdp();

    Stream<S> list();
}
```

## UDP

::: danger UDP 类型的客户端会话如何处理？

**UDP** 是无状态的；也就是说 **并没有** 一个持久的连接。

**Xtream** 对 **UDP** 会话的默认实现逻辑是：

- 和 **TCP** 一样放在 `XtreamSessionManager` 中
- 维护一个定时器，定时检查会话是否过期(最后一次通信时间)，过期则从 `XtreamSessionManager` 中移除并调用监听器的 `beforeSessionClose` 方法

:::

## XtreamSessionEventListener

`XtreamSessionEventListener` 会在 `XtreamSession` 创建和销毁时回调。

接口定义如下：

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
