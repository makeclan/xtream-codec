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

import io.github.classgraph.ClassInfo;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author hylexus
 */
public interface XtreamClassScanner {

    enum ScanMode {
        CLASS_ANNOTATION,
        FIELD_ANNOTATION,
        METHOD_ANNOTATION,
    }

    default Set<Class<?>> scan(String[] basePackages, Set<ScanMode> scanMode, Class<? extends Annotation> annotationClass) {
        return this.scan(basePackages, scanMode, annotationClass, classInfo -> true);
    }

    Set<Class<?>> scan(String[] basePackages, Set<ScanMode> scanMode, Class<? extends Annotation> annotationClass, Predicate<ClassInfo> predicate);
}
