package io.github.hylexus.xtream.debug.ext.jt808.service;

import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0200;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
import reactor.core.publisher.Mono;

public interface DemoLocationService {

    Mono<Byte> processLocationMessage(XtreamResponse response, Jt808Session session, BuiltinMessage0200 body);

}
