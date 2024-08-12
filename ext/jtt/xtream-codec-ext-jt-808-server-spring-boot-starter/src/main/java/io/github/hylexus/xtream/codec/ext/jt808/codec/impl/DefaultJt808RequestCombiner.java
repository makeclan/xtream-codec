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

package io.github.hylexus.xtream.codec.ext.jt808.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageEncryptionType;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DefaultJt808RequestCombiner implements Jt808RequestCombiner {
    protected final SimpleCache cache;
    protected final ByteBufAllocator allocator;

    public DefaultJt808RequestCombiner(ByteBufAllocator allocator, int maximumCacheSize, Duration cacheItemTtl) {
        this.cache = new SimpleCache(maximumCacheSize, cacheItemTtl);
        this.allocator = allocator;
    }

    @Nullable
    @Override
    public Jt808Request tryMergeSubPackage(Jt808Request jt808Request) {
        final String key = this.buildSubPackageCacheKey(jt808Request.header());
        final Map<Integer, CacheItem> subPackages = this.cacheAndGetSubPackages(key, jt808Request);

        // 所有子包都已经到达
        if (subPackages.size() == jt808Request.header().subPackage().totalSubPackageCount()) {
            return this.mergeRequest(key, subPackages, jt808Request);
        }
        // 还有子包没到达 ==> null
        return null;
    }

    private Jt808Request mergeRequest(String key, Map<Integer, CacheItem> subPackages, Jt808Request jt808Request) {
        final List<CacheItem> allPackages = subPackages.values().stream()
                .sorted(Comparator.comparing(it -> it.header.subPackage().currentPackageNo()))
                .toList();

        final CompositeByteBuf compositeByteBuf = this.allocator.compositeBuffer(subPackages.size());
        try {
            int totalLength = 0;
            for (final CacheItem subPackage : allPackages) {
                compositeByteBuf.addComponents(true, subPackage.temporarySubPackageBodyByteBuf);
                // 累加中间包的长度(下面重新生成一个新的 `消息体属性`)
                totalLength += subPackage.header.messageBodyLength();
            }
            final Jt808RequestHeader oldHeader = jt808Request.header();
            final Jt808RequestHeader.Jt808MessageBodyProps newMessageBodyProps = oldHeader.messageBodyProps().mutate()
                    .messageBodyLength(totalLength)
                    .encryptionType(Jt808MessageEncryptionType.DEFAULT_ENCRYPTION_TYPE)
                    .hasSubPackage(false)
                    .build();

            final Jt808RequestHeader newHeader = Jt808RequestHeader.newBuilder()
                    .messageId(oldHeader.messageId())
                    .messageBodyProps(newMessageBodyProps)
                    .version(oldHeader.version())
                    .terminalId(oldHeader.terminalId())
                    .build();

            return jt808Request.mutate()
                    .header(newHeader)
                    .body(compositeByteBuf, false)
                    .originalCheckSum(jt808Request.originalCheckSum())
                    .calculatedCheckSum(0)
                    .build();
        } finally {
            this.cache.invalidate(key);
        }
    }

    private Map<Integer, CacheItem> cacheAndGetSubPackages(String key, Jt808Request jt808Msg) {
        final Map<Integer, CacheItem> subPackages = this.cache.get(key, k -> new ConcurrentHashMap<>());
        final CacheItem cacheItem = new CacheItem(jt808Msg.header(), jt808Msg.body().retain());
        final CacheItem old = subPackages.put(jt808Msg.header().subPackage().currentPackageNo(), cacheItem);
        if (old != null) {
            XtreamBytes.releaseBuf(old.temporarySubPackageBodyByteBuf);
        }
        return subPackages;
    }

    protected String buildSubPackageCacheKey(Jt808RequestHeader request) {
        return String.format("%s_%d_%d", request.terminalId(), request.messageId(), request.subPackage().totalSubPackageCount());
    }

    public record CacheItem(Jt808RequestHeader header, ByteBuf temporarySubPackageBodyByteBuf) {
    }

    public static class SimpleCache {
        private static final Logger log = LoggerFactory.getLogger(SimpleCache.class);
        protected final Cache<String, Map<Integer, CacheItem>> cache;
        public static final String LOG_PREFIX = "<SubPackage>";

        public SimpleCache(int maximumSize, Duration ttl) {
            this.cache = Caffeine.newBuilder()
                    .maximumSize(maximumSize)
                    .expireAfterWrite(ttl)
                    .removalListener((RemovalListener<String, Map<Integer, CacheItem>>) (key, value, cause) -> {
                        if (value == null) {
                            return;
                        }
                        final boolean isExpired = cause == RemovalCause.EXPIRED
                                || cause == RemovalCause.SIZE
                                || cause == RemovalCause.COLLECTED;
                        if (isExpired) {
                            value.forEach((currentPackageNo, subPackage) -> {
                                XtreamBytes.releaseBuf(subPackage.temporarySubPackageBodyByteBuf);
                                log.info("{} {} has been released. reason = {}", LOG_PREFIX, subPackage, cause);
                            });
                        }

                    }).build();
        }

        public Map<Integer, CacheItem> get(String key, Function<String, Map<Integer, CacheItem>> loader) {
            return this.cache.get(key, loader);
        }

        public void invalidate(String key) {
            this.cache.invalidate(key);
        }
    }
}
