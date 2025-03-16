/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.ext.jt1078.codec.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.Jt1078RequestCombiner;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078SubPackageIdentifier;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.NettyInbound;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CaffeineJt1078RequestCombiner implements Jt1078RequestCombiner {
    protected final SimpleCache cache;
    private final ByteBufAllocator allocator;

    public CaffeineJt1078RequestCombiner(ByteBufAllocator allocator, int maximumSize, Duration ttl) {
        this.allocator = allocator;
        this.cache = new SimpleCache(maximumSize, ttl);
    }

    @Override
    public String getTraceId(NettyInbound nettyInbound, Jt1078RequestHeader header) {
        final Jt1078SubPackageIdentifier identifier = header.subPackageIdentifier();
        return switch (identifier) {
            case ATOMIC, FIRST -> Jt1078RequestCombiner.randomTraceId();
            case MIDDLE, LAST -> {
                final String key = this.buildSubPackageCacheKey(header);
                final List<CacheItem> list = this.cache.getIfPresent(key);
                if (list == null || list.isEmpty()) {
                    yield Jt1078RequestCombiner.randomTraceId();
                }
                yield list.getFirst().traceId();
            }
        };
    }

    @Nullable
    @Override
    public Jt1078Request tryMergeSubPackage(Jt1078Request request) {
        final Jt1078SubPackageIdentifier identifier = request.header().subPackageIdentifier();
        return switch (identifier) {
            case null -> throw new IllegalStateException("SubPackageIdentifier is null");
            case FIRST, MIDDLE -> this.writeCache(request);
            case LAST -> this.mergeRequest(request);
            // 原子包: 不应该执行到这里
            case ATOMIC -> request;
        };
    }

    private Jt1078Request writeCache(Jt1078Request request) {
        final String key = buildSubPackageCacheKey(request.header());
        final List<CacheItem> list = this.cache.get(key, k -> new ArrayList<>());
        final CacheItem cacheItem = new CacheItem(request.traceId(), request.header(), request.body().retain());
        list.add(cacheItem);
        return null;
    }

    private Jt1078Request mergeRequest(Jt1078Request currentRequest) {
        final String key = this.buildSubPackageCacheKey(currentRequest.header());
        final List<CacheItem> list = this.cache.getIfPresent(key);
        if (list == null) {
            throw new IllegalStateException("No prev package found.");
        }
        int newBodyLength = 0;
        final CompositeByteBuf newBody = allocator.compositeBuffer();
        try {
            for (final CacheItem item : list) {
                newBodyLength += item.body.readableBytes();
                newBody.addComponent(true, item.body);
            }
            newBody.addComponent(true, currentRequest.body().retain());
            newBodyLength += currentRequest.messageBodyLength();

            final Jt1078RequestHeader newHeader = currentRequest.header().mutate()
                    .msgBodyLength(newBodyLength)
                    .subPackageIdentifier(Jt1078SubPackageIdentifier.ATOMIC)
                    .isCombined(true)
                    .build();
            final Jt1078Request newRequest = currentRequest.mutate()
                    .header(newHeader)
                    .body(newBody, false)
                    .build();
            list.clear();
            this.cache.invalidate(key);
            return newRequest;
        } catch (Exception e) {
            XtreamBytes.releaseBuf(newBody);
            throw e;
        }
    }

    protected String buildSubPackageCacheKey(Jt1078RequestHeader request) {
        return request.sim() + "_" + request.channelNumber();
    }

    public record CacheItem(String traceId, Jt1078RequestHeader header, ByteBuf body) {
    }

    public static class SimpleCache {
        private static final Logger log = LoggerFactory.getLogger(SimpleCache.class);
        protected final Cache<String, List<CacheItem>> cache;
        public static final String LOG_PREFIX = "<SubPackage>";

        public SimpleCache(int maximumSize, Duration ttl) {
            this.cache = Caffeine.newBuilder()
                    .maximumSize(maximumSize)
                    .expireAfterWrite(ttl)
                    .removalListener((RemovalListener<String, List<CacheItem>>) (key, value, cause) -> {
                        if (value == null) {
                            return;
                        }
                        final boolean isExpired = cause == RemovalCause.EXPIRED
                                                  || cause == RemovalCause.SIZE
                                                  || cause == RemovalCause.COLLECTED;
                        if (isExpired) {
                            value.forEach(item -> {
                                XtreamBytes.releaseBuf(item.body);
                                log.info("{} {} has been released. reason = {}", LOG_PREFIX, key, cause);
                            });
                        }

                    }).build();
        }

        public List<CacheItem> get(String key, Function<String, List<CacheItem>> loader) {
            return this.cache.get(key, loader);
        }

        public List<CacheItem> getIfPresent(String key) {
            return this.cache.getIfPresent(key);
        }

        public void invalidate(String key) {
            this.cache.invalidate(key);
        }
    }
}
