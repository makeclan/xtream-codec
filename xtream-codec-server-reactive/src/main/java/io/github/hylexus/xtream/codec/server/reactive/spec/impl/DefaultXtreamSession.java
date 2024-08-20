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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;


import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author hylexus
 */
public class DefaultXtreamSession implements XtreamSession {

    protected final String id;
    protected final XtreamRequest.Type type;
    protected final Map<String, Object> attributes = new HashMap<>();

    public DefaultXtreamSession(XtreamRequest.Type type) {
        this(generateId(), type);
    }

    public DefaultXtreamSession(String id, XtreamRequest.Type type) {
        this.id = id;
        this.type = type;
    }

    private static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public XtreamRequest.Type type() {
        return this.type;
    }

    @Override
    public Map<String, Object> attributes() {
        return this.attributes;
    }
}
