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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.core.annotation.OrderedComponent;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamFilter;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamHandler;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamNettyHandlerAdapter;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.*;
import io.github.hylexus.xtream.codec.server.reactive.spec.handler.builtin.DelegateXtreamHandlerMethodArgumentResolver;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamHandlerAdapterBuilder<C extends AbstractXtreamHandlerAdapterBuilder<C, A>, A extends XtreamNettyHandlerAdapter> {
    private static final Logger log = LoggerFactory.getLogger(AbstractXtreamHandlerAdapterBuilder.class);
    protected final ByteBufAllocator byteBufAllocator;
    private final List<XtreamHandlerMapping> handlerMappings;

    private final List<XtreamHandlerAdapter> handlerAdapters;

    private final List<XtreamHandlerResultHandler> resultHandlers;

    private final List<XtreamFilter> xtreamFilters;
    private final List<XtreamRequestExceptionHandler> exceptionHandlers;


    public AbstractXtreamHandlerAdapterBuilder() {
        this(ByteBufAllocator.DEFAULT);
    }

    public AbstractXtreamHandlerAdapterBuilder(ByteBufAllocator allocator) {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
        this.resultHandlers = new ArrayList<>();
        this.xtreamFilters = new ArrayList<>();
        this.exceptionHandlers = new ArrayList<>();
        this.byteBufAllocator = allocator;
    }

    @SuppressWarnings("unchecked")
    private C self() {
        return (C) this;
    }

    public C addHandlerMapping(XtreamHandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
        return self();
    }

    public C removeHandlerMapping(Class<? extends XtreamHandlerMapping> cls) {
        this.handlerMappings.removeIf(m -> m.getClass().equals(cls));
        return self();
    }

    public C enableBuiltinHandlerAdapters() {
        return this.enableBuiltinHandlerAdapters(DelegateXtreamHandlerMethodArgumentResolver.INSTANCE);
    }

    public C enableBuiltinHandlerAdapters(XtreamHandlerMethodArgumentResolver argumentResolver) {
        this.addHandlerAdapter(new XtreamHandlerMethodHandlerAdapter(argumentResolver));
        this.addHandlerAdapter(new SimpleXtreamRequestHandlerHandlerAdapter());
        return self();
    }

    public C disableBuiltinHandlerAdapters() {
        this.removeHandlerAdapter(XtreamHandlerMethodHandlerAdapter.class);
        this.removeHandlerAdapter(SimpleXtreamRequestHandlerHandlerAdapter.class);
        return self();
    }

    public C enableBuiltinHandlerResultHandlers() {
        this.addHandlerResultHandler(new XtreamResponseBodyHandlerResultHandler());
        this.addHandlerResultHandler(new LoggingXtreamHandlerResultHandler());
        return self();
    }

    public C disableBuiltinHandlerResultHandlers() {
        this.removeResultHandler(LoggingXtreamHandlerResultHandler.class);
        this.removeResultHandler(XtreamResponseBodyHandlerResultHandler.class);
        return self();
    }

    public C addHandlerAdapter(XtreamHandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
        return self();
    }

    public C removeHandlerAdapter(Class<? extends XtreamHandlerAdapter> cls) {
        this.handlerAdapters.removeIf(m -> m.getClass().equals(cls));
        return self();
    }

    public C addHandlerResultHandler(XtreamHandlerResultHandler resultHandler) {
        this.resultHandlers.add(resultHandler);
        return self();
    }

    public C removeResultHandler(Class<? extends XtreamHandlerResultHandler> cls) {
        this.resultHandlers.removeIf(m -> m.getClass().equals(cls));
        return self();
    }

    public C addFilter(XtreamFilter filter) {
        this.xtreamFilters.add(filter);
        return self();
    }

    public C removeFilter(Class<? extends XtreamFilter> cls) {
        this.xtreamFilters.removeIf(m -> m.getClass().equals(cls));
        return self();
    }

    public C addExceptionHandler(XtreamRequestExceptionHandler exceptionHandler) {
        this.exceptionHandlers.add(exceptionHandler);
        return self();
    }

    public C removeExceptionHandler(Class<? extends XtreamRequestExceptionHandler> cls) {
        this.exceptionHandlers.removeIf(m -> m.getClass().equals(cls));
        return self();
    }

    public abstract A build();

    protected XtreamHandler createRequestHandler() {
        if (this.handlerMappings.isEmpty()) {
            throw new IllegalStateException("No [" + XtreamHandlerMapping.class.getSimpleName() + "] instance configured.");
        }

        if (this.handlerAdapters.isEmpty()) {
            throw new IllegalStateException("No [" + XtreamHandlerAdapter.class.getSimpleName() + "] instance configured.");
        }

        if (this.resultHandlers.isEmpty()) {
            log.info("No [{}] instance configured. Add [{}] for debugging purpose.", XtreamHandlerResultHandler.class.getSimpleName(), LoggingXtreamHandlerResultHandler.class.getSimpleName());
            this.resultHandlers.add(new LoggingXtreamHandlerResultHandler());
        }

        final DispatcherXtreamHandler dispatcherHandler = new DispatcherXtreamHandler(
                OrderedComponent.sort(this.handlerMappings),
                OrderedComponent.sort(this.handlerAdapters),
                OrderedComponent.sort(this.resultHandlers)
        );

        final FilteringXtreamHandler filteringHandler = new FilteringXtreamHandler(
                dispatcherHandler,
                OrderedComponent.sort(this.xtreamFilters)
        );

        return new ExceptionHandlingXtreamHandler(
                filteringHandler,
                OrderedComponent.sort(this.exceptionHandlers)
        );
    }
}
