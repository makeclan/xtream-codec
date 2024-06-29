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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;


import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author hylexus
 */
public abstract class AbstractXtreamSession implements XtreamSession {

    protected final String id;

    protected final Map<String, Object> attributes = new HashMap<>();

    public AbstractXtreamSession() {
        this(generateId());
    }

    public AbstractXtreamSession(String id) {
        this.id = id;
    }

    private static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Map<String, Object> attributes() {
        return this.attributes;
    }
}
