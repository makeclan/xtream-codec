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

package io.github.hylexus.xtream.codec.common.utils;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ReferenceCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 */
public class XtreamUtils {
    public static final ByteBufAllocator DEFAULT_BUFFER_FACTORY = ByteBufAllocator.DEFAULT;
    private static final Logger log = LoggerFactory.getLogger(XtreamUtils.class);

    public static boolean hasElement(String str) {
        return str != null && !str.isEmpty();
    }

    public static void release(Object... objects) {
        if (objects == null) {
            return;
        }
        for (Object object : objects) {
            if (object == null) {
                continue;
            }
            try {
                if (object instanceof ReferenceCounted referenceCounted) {
                    if (referenceCounted.refCnt() > 0) {
                        referenceCounted.release();
                    }
                }
            } catch (Throwable e) {
                log.error("", e);
            }
        }
    }

    public static String detectMainClassPackageName() {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final List<String> candidates = new ArrayList<>();
        for (StackTraceElement element : stackTraceElements) {
            if ("main".equals(element.getMethodName())) {
                final String className = element.getClassName();
                final int lastDotIndex = className.lastIndexOf('.');
                final String packageName = lastDotIndex > 0 ? className.substring(0, lastDotIndex) : "";
                candidates.add(packageName);
            }
        }
        if (candidates.isEmpty()) {
            throw new IllegalStateException("Cannot determine Main class");
        }
        return candidates.getLast();
    }

    public static int setBitRange(int from, int length, int target, int offset) {
        return ((~(((1 << length) - 1) << offset)) & target)
                |
                (from << offset);
    }

}
