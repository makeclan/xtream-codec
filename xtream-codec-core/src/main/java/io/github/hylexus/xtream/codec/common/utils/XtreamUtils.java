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

import io.netty.util.ReferenceCounted;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
public class XtreamUtils {
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
}
