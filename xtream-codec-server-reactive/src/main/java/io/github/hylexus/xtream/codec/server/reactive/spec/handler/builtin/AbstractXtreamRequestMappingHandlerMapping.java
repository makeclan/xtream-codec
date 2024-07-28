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

package io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin;

import io.github.hylexus.xtream.codec.common.utils.DefaultXtreamClassScanner;
import io.github.hylexus.xtream.codec.common.utils.XtreamClassScanner;
import io.github.hylexus.xtream.codec.common.utils.XtreamUtils;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.ReactiveXtreamHandlerMethod;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamHandlerMethod;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamRequestMapping;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.XtreamHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamRequestMappingHandlerMapping implements XtreamHandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AbstractXtreamRequestMappingHandlerMapping.class);
    protected List<XtreamHandlerMethod> handlerMethods = new ArrayList<>();

    public AbstractXtreamRequestMappingHandlerMapping(String[] basePackages, Function<Class<?>, Object> instanceFactory) {
        this.initHandlerMethods(basePackages, instanceFactory);
    }

    protected Set<Class<?>> doScan(String[] basePackages) {
        return new DefaultXtreamClassScanner().scan(
                basePackages,
                Set.of(XtreamClassScanner.ScanMode.METHOD_ANNOTATION),
                XtreamRequestMapping.class,
                classInfo -> !classInfo.isAnnotation()
        );
    }

    protected void initHandlerMethods(String[] basePackages, Function<Class<?>, Object> instanceFactory) {
        // 启动类所在包名
        if (basePackages == null || basePackages.length == 0) {
            final String packageName = XtreamUtils.detectMainClassPackageName();
            log.info("Use [{}] as [@{}] scan packages.", packageName, XtreamRequestMapping.class.getSimpleName());
            basePackages = new String[]{packageName};
        }

        final Set<Class<?>> classes = this.doScan(basePackages);

        for (final Class<?> cls : classes) {
            ReflectionUtils.doWithMethods(cls, method -> {
                final XtreamHandlerMethod handlerMethod = new ReactiveXtreamHandlerMethod(cls, method);
                final Object containerInstance = instanceFactory.apply(cls);
                handlerMethod.setContainerInstance(containerInstance);
                handlerMethods.add(handlerMethod);
            });
        }
    }

    @Override
    public int order() {
        return -1;
    }
}
