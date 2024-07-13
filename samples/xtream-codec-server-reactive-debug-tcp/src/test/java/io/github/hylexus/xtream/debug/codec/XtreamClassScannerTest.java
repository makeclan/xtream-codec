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

package io.github.hylexus.xtream.debug.codec;

import io.github.hylexus.xtream.codec.common.utils.DefaultXtreamClassScanner;
import io.github.hylexus.xtream.codec.common.utils.XtreamClassScanner;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamRequestMapping;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class XtreamClassScannerTest {

    // @Test
    void test() {
        final String pkg = "io.github.hylexus.xtream.debug.codec.server.reactive.tcp";
        // final Class<XtreamField> annotation = XtreamField.class;
        final Class<XtreamRequestMapping> annotation = XtreamRequestMapping.class;
        final XtreamClassScanner scanner = new DefaultXtreamClassScanner();
        System.out.println("===".repeat(10));
        final Set<Class<?>> result = scanner.scan(
                new String[]{pkg},
                Set.of(
                        XtreamClassScanner.ScanMode.CLASS_ANNOTATION,
                        XtreamClassScanner.ScanMode.METHOD_ANNOTATION,
                        XtreamClassScanner.ScanMode.FIELD_ANNOTATION
                ),
                annotation, classInfo -> {
                    System.out.println(classInfo + " __--__" + classInfo.isAnnotation());
                    return classInfo.getName().startsWith(pkg) && !classInfo.isInnerClass();
                });
        for (final Class<?> aClass : result) {
            System.out.println(aClass);
        }
    }
}
