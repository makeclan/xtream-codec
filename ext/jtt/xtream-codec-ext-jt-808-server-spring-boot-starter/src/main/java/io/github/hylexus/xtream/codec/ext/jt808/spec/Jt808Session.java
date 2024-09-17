package io.github.hylexus.xtream.codec.ext.jt808.spec;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;

import java.time.Instant;

/**
 * 这个接口是 {@code sealed} 的。
 * <p>
 * 用户自定义实现类应该实现 {@link Jt808Session.MutableJt808Session} 而不是 {@link Jt808Session}。
 * <p>
 * 业务编程时建议注入 {@link Jt808Session}, 而不是 {@link Jt808Session.MutableJt808Session}
 */
public sealed interface Jt808Session extends XtreamSession
        permits Jt808Session.MutableJt808Session {

    Jt808ServerType role();

    Instant creationTime();

    /**
     * @return 上次通信时间
     */
    Instant lastCommunicateTime();

    Jt808Session lastCommunicateTime(Instant current);

    boolean verified();

    Jt808Session verified(boolean verified);

    // 下面几个属性在请求真正被解码之后才有值
    String terminalId();

    Jt808ProtocolVersion protocolVersion();

    non-sealed interface MutableJt808Session extends Jt808Session {

        MutableJt808Session protocolVersion(Jt808ProtocolVersion protocolVersion);

        MutableJt808Session terminalId(String terminalId);

    }
}
