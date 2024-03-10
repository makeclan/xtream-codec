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

package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.utils.BeanUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Constructor;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BeanMetadata {
    private final Class<?> rawType;
    private final Constructor<?> constructor;
    private final List<BeanPropertyMetadata> propertyMetadataList;

    public BeanMetadata(Class<?> rawType, Constructor<?> constructor, List<BeanPropertyMetadata> propertyMetadataList) {
        this.rawType = rawType;
        this.constructor = constructor;
        this.propertyMetadataList = propertyMetadataList;
    }

    public Object createNewInstance() {
        return BeanUtils.createNewInstance(this.getConstructor(), (Object[]) null);
    }
}
