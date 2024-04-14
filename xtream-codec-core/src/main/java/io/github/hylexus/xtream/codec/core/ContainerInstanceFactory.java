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

package io.github.hylexus.xtream.codec.core;

import io.github.hylexus.xtream.codec.common.exception.NotYetImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface ContainerInstanceFactory {

    Object create();

    ContainerInstanceFactory PLACEHOLDER = new PlaceholderContainerInstanceFactory();

    class PlaceholderContainerInstanceFactory implements ContainerInstanceFactory {

        @Override
        public Object create() {
            throw new NotYetImplementedException();
        }
    }

    // region java.util.Map
    class HashMapContainerInstanceFactory implements ContainerInstanceFactory {

        @Override
        public Object create() {
            return new HashMap<>();
        }
    }

    class LinkedHashMapContainerInstanceFactory implements ContainerInstanceFactory {

        @Override
        public Object create() {
            return new LinkedHashMap<>();
        }
    }
    // endregion java.util.Map

    // region java.util.List
    class ArrayListContainerInstanceFactory implements ContainerInstanceFactory {

        @Override
        public Object create() {
            return new ArrayList<>();
        }
    }

    class LinkedListContainerInstanceFactory implements ContainerInstanceFactory {

        @Override
        public Object create() {
            return new LinkedList<>();
        }
    }
    // endregion java.util.List
}
