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

package io.github.hylexus.xtream.codec.core.type;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.Set;

import static java.util.Set.of;

@Getter
@Accessors(fluent = true)
public enum XtreamDataType {

    i8(1, of(byte.class)),
    u8(1, of(short.class)),
    i16(2, of(short.class)),
    i16_le(2, of(short.class)),
    u16(2, of(int.class)),
    u16_le(2, of(int.class)),
    i32(4, of(int.class)),
    i32_le(4, of(int.class)),
    u32(4, of(long.class)),
    u32_le(4, of(long.class)),
    f32(4, of(Float.class)),
    f32_le(4, of(Float.class)),
    i64(8, of(long.class)),
    i64_le(8, of(long.class)),
    u64(8, of(BigInteger.class)),
    u64_le(8, of(BigInteger.class)),
    f64(8, of(Double.class)),
    f64_le(8, of(Double.class)),
    i128(16, of(BigInteger.class)),
    i128_le(16, of(BigInteger.class)),
    u128(16, of(BigInteger.class)),
    u128_le(16, of(BigInteger.class)),
    string(-1, of(String.class)),
    ;
    private final int sizeInBytes;
    private final Set<Class<?>> recommendedJavaTypes;

    XtreamDataType(int sizeInBytes, Set<Class<?>> recommendedJavaTypes) {
        this.sizeInBytes = sizeInBytes;
        this.recommendedJavaTypes = recommendedJavaTypes;
    }
}
