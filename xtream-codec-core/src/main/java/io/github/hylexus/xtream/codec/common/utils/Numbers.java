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

package io.github.hylexus.xtream.codec.common.utils;

public final class Numbers {
    private Numbers() {
    }

    public static int getBitAt(long value, int index) {
        return (int) ((1L << index & value) >>> index);
    }

    public static long setBitAt(long value, int index) {
        return 1L << index | value;
    }

    public static long resetBitAt(long value, int index) {
        return value & ~(1L << index);
    }

}
