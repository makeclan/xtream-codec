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

package io.github.hylexus.xtream.debug.codec.core.demo04.spec;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.xtream.codec.common.utils.XtreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class SimpleCache {
    protected final Cache<String, Map<Integer, BaseJt808Msg>> cache;
    public static final String LOG_PREFIX = "<SubPackage>";

    public SimpleCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(1024)
                .expireAfterWrite(Duration.ofSeconds(45))
                .removalListener((RemovalListener<String, Map<Integer, BaseJt808Msg>>) (key, value, cause) -> {
                    if (value == null) {
                        return;
                    }
                    final boolean isExpired = cause == RemovalCause.EXPIRED
                            || cause == RemovalCause.SIZE
                            || cause == RemovalCause.COLLECTED;
                    if (isExpired) {
                        value.forEach((currentPackageNo, subPackage) -> {
                            XtreamUtils.release(subPackage.getInternalTemporarySubPackageBodyByteBuf());
                            log.info("{} {} has been released. reason = {}", LOG_PREFIX, subPackage, cause);
                        });
                    }

                }).build();
    }

    public Map<Integer, BaseJt808Msg> get(String key, Function<String, Map<Integer, BaseJt808Msg>> loader) {
        return this.cache.get(key, loader);
    }

    public void invalidate(String key) {
        this.cache.invalidate(key);
    }
}
