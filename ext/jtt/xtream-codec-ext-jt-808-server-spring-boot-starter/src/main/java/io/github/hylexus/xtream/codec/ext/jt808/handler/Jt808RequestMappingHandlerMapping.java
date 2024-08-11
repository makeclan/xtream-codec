/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.ext.jt808.handler;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.ReactiveXtreamHandlerMethod;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamHandlerMethod;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
public class Jt808RequestMappingHandlerMapping implements XtreamHandlerMapping, ApplicationContextAware, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(Jt808RequestMappingHandlerMapping.class);
    // <messageId,<version,handler>>
    private final Map<Integer, Map<Jt808ProtocolVersion, XtreamHandlerMethod>> mappings = new HashMap<>();
    protected ApplicationContext applicationContext;

    public Jt808RequestMappingHandlerMapping() {
    }

    @Override
    public Mono<Object> getHandler(XtreamExchange exchange) {
        if (exchange.request() instanceof Jt808Request jt808Request) {
            final Map<Jt808ProtocolVersion, XtreamHandlerMethod> multiVersionHandlers = mappings.get(jt808Request.header().messageId());
            if (multiVersionHandlers == null || multiVersionHandlers.isEmpty()) {
                return Mono.empty();
            }
            final XtreamHandlerMethod handler = multiVersionHandlers.get(jt808Request.header().version());
            if (handler == null) {
                return Mono.justOrEmpty(multiVersionHandlers.get(Jt808ProtocolVersion.AUTO_DETECTION));
            }
            return Mono.just(handler);
        }
        return Mono.empty();
    }

    protected void initHandlerMethods() {
        final Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Jt808RequestHandler.class);

        for (final Map.Entry<String, Object> entry : beans.entrySet()) {
            final Object instance = entry.getValue();
            final Class<?> cls = instance.getClass();
            ReflectionUtils.doWithMethods(
                    cls,
                    method -> {
                        final XtreamHandlerMethod handlerMethod = new ReactiveXtreamHandlerMethod(cls, method);
                        handlerMethod.setContainerInstance(instance);
                        final Jt808RequestHandlerMapping annotation = AnnotatedElementUtils.getMergedAnnotation(method, Jt808RequestHandlerMapping.class);
                        if (annotation != null) {
                            final int[] messageIds = annotation.messageIds();
                            for (final int messageId : messageIds) {
                                final Map<Jt808ProtocolVersion, XtreamHandlerMethod> map = this.mappings.computeIfAbsent(messageId, k -> new HashMap<>());
                                for (final Jt808ProtocolVersion version : annotation.versions()) {
                                    map.put(version, handlerMethod);
                                }
                            }
                        }
                    },
                    // 被 @Jt808RequestHandlerMapping 注解的方法才会被注册为处理器
                    method -> AnnotatedElementUtils.hasAnnotation(method, Jt808RequestHandlerMapping.class)
            );
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initHandlerMethods();
    }
}
