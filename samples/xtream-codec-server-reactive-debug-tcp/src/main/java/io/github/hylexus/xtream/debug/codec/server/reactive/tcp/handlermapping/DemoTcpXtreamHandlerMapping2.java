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

package io.github.hylexus.xtream.debug.codec.server.reactive.tcp.handlermapping;

import io.github.hylexus.xtream.codec.common.utils.XtreamUtils;
import io.github.hylexus.xtream.codec.core.utils.BeanUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.AbstractSimpleXtreamRequestMappingHandlerMapping;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.DefaultXtreamBlockingHandlerMethodPredicate;
import io.github.hylexus.xtream.codec.server.reactive.spec.resources.DefaultXtreamSchedulerRegistry;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

/**
 * @author hylexus
 */
public class DemoTcpXtreamHandlerMapping2 extends AbstractSimpleXtreamRequestMappingHandlerMapping {

    public DemoTcpXtreamHandlerMapping2() {
        this(new String[]{XtreamUtils.detectMainClassPackageName()}, cls -> BeanUtils.createNewInstance(cls, new Object[0]));
    }

    public DemoTcpXtreamHandlerMapping2(String[] basePackages, Function<Class<?>, Object> instanceFactory) {
        super(
                new DefaultXtreamSchedulerRegistry(Schedulers.parallel(), Schedulers.boundedElastic()),
                new DefaultXtreamBlockingHandlerMethodPredicate(),
                basePackages, instanceFactory
        );
    }

    @Override
    public Mono<Object> getHandler(XtreamExchange exchange) {
        // 这里示例性地返回第一个处理器，你可以自定义逻辑来进行分发
        return Mono.just(handlerMethods.getFirst());
    }

}
