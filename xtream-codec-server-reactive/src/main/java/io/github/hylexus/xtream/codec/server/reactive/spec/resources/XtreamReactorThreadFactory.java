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

package io.github.hylexus.xtream.codec.server.reactive.spec.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.NonBlocking;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * 当前类是从 `reactor.core.scheduler.ReactorThreadFactory` 复制过来修改的。
 * <p>
 * The current class is derived from and modified based on `reactor.core.scheduler.ReactorThreadFactory`.
 *
 * @author hylexus
 */
public class XtreamReactorThreadFactory implements ThreadFactory,
        Thread.UncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(XtreamReactorThreadFactory.class);
    private final String name;
    private final AtomicLong counterReference = new AtomicLong();
    private final boolean daemon;
    private final boolean rejectBlocking;

    private final BiConsumer<Thread, Throwable> uncaughtExceptionHandler;

    public XtreamReactorThreadFactory(
            String name,
            boolean daemon,
            boolean rejectBlocking) {

        this(name, daemon, rejectBlocking, null);
    }

    public XtreamReactorThreadFactory(
            String name,
            boolean daemon,
            boolean rejectBlocking,
            @Nullable BiConsumer<Thread, Throwable> uncaughtExceptionHandler) {
        this.name = name;
        this.daemon = daemon;
        this.rejectBlocking = rejectBlocking;
        this.uncaughtExceptionHandler = Objects.requireNonNullElseGet(
                uncaughtExceptionHandler,
                () -> (thread, throwable) -> {
                    // ...
                    log.error("Scheduler worker in group {} failed with an uncaught exception", thread.getThreadGroup().getName(), throwable);
                }
        );
    }

    @Override
    public final Thread newThread(@NonNull Runnable runnable) {
        String newThreadName = name + "-" + counterReference.incrementAndGet();
        Thread t = rejectBlocking
                ? new NonBlockingThread(runnable, newThreadName)
                : new Thread(runnable, newThreadName);
        if (daemon) {
            t.setDaemon(true);
        }
        if (uncaughtExceptionHandler != null) {
            t.setUncaughtExceptionHandler(this);
        }
        return t;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (uncaughtExceptionHandler == null) {
            return;
        }

        uncaughtExceptionHandler.accept(t, e);
    }

    static final class NonBlockingThread extends Thread implements NonBlocking {

        public NonBlockingThread(Runnable target, String name) {
            super(target, name);
        }

    }
}
