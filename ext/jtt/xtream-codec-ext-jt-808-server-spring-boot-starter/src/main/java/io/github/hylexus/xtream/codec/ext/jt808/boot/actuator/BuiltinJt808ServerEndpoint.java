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

package io.github.hylexus.xtream.codec.ext.jt808.boot.actuator;

import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808ServerSimpleMetricsHolder;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Endpoint(id = "jt808", enableByDefault = false)
public class BuiltinJt808ServerEndpoint {

    private final XtreamJt808ServerProperties serverProperties;
    private final Jt808ServerSimpleMetricsHolder collector;

    public BuiltinJt808ServerEndpoint(XtreamJt808ServerProperties serverProperties, Jt808ServerSimpleMetricsHolder collector) {
        this.collector = collector;
        this.serverProperties = serverProperties;
    }

    @ReadOperation
    public List<String> types() {
        return List.of("metrics", "config");
    }

    @ReadOperation
    public Object type(@Selector String type) {
        return switch (type) {
            case "metrics" -> collector;
            case "config" -> this.serverProperties;
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }


}
