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

/**
 * @author hylexus
 */
public abstract class XtreamAssertions {

    private XtreamAssertions() {
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message);
        }
    }

    public static void assertSame(Object expected, Object actual) {
        assertSame(expected, actual, "Expected: " + expected + ", but was: " + actual);
    }

    public static void assertSame(Object expected, Object actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message);
        }
    }

    public static void assertNotSame(Object expected, Object actual) {
        assertNotSame(expected, actual, "Expected: not same but was: <" + actual + ">");
    }

    public static void assertNotSame(Object expected, Object actual, String message) {
        if (expected == actual) {
            throw new AssertionError(message);
        }
    }
}
