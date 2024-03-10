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
