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

package io.github.hylexus.xtream.codec.core.annotation;

import java.util.Comparator;
import java.util.List;

/**
 * @author hylexus
 */
public interface OrderedComponent {
    int BUILTIN_COMPONENT_PRECEDENCE = 1000000;
    int DEFAULT_PRECEDENCE = 0;
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    default int order() {
        return DEFAULT_PRECEDENCE;
    }

    static <T extends OrderedComponent> List<T> sort(List<T> components) {
        return components.stream()
                .sorted(Comparator.comparingInt(OrderedComponent::order))
                .toList();
    }
}
