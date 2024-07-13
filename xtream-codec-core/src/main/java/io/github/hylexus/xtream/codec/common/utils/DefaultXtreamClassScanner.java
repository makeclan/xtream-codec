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

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
public class DefaultXtreamClassScanner implements XtreamClassScanner {
    public DefaultXtreamClassScanner() {
    }

    @Override
    public Set<Class<?>> scan(String[] basePackages, Set<ScanMode> scanMode, Class<? extends Annotation> annotationClass, Predicate<ClassInfo> predicate) {
        final ClassGraph classGraph = new ClassGraph()
                .verbose(false)
                .enableAllInfo()
                .acceptPackages(basePackages);

        final Set<String> result = new HashSet<>();
        try (ScanResult scanResult = classGraph.scan()) {

            if (scanMode.contains(ScanMode.CLASS_ANNOTATION)) {
                for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(annotationClass)) {
                    if (predicate.test(classInfo)) {
                        result.add(classInfo.getName());
                    }
                }
            }

            if (scanMode.contains(ScanMode.FIELD_ANNOTATION)) {
                for (final ClassInfo classInfo : scanResult.getClassesWithFieldAnnotation(annotationClass)) {
                    if (predicate.test(classInfo)) {
                        result.add(classInfo.getName());
                    }
                }
            }

            if (scanMode.contains(ScanMode.METHOD_ANNOTATION)) {
                for (final ClassInfo classInfo : scanResult.getClassesWithMethodAnnotation(annotationClass)) {
                    if (predicate.test(classInfo)) {
                        result.add(classInfo.getName());
                    }
                }
            }

        }

        return result.stream().map(this::loadClass).collect(Collectors.toSet());
    }

    Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
