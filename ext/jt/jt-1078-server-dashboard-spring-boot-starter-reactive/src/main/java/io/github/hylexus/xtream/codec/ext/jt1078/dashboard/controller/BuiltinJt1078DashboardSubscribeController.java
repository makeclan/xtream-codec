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

package io.github.hylexus.xtream.codec.ext.jt1078.dashboard.controller;

import io.github.hylexus.xtream.codec.base.web.domain.dto.PageableDto;
import io.github.hylexus.xtream.codec.base.web.domain.vo.PageableVo;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078SubscriberDescriptor;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078SubscriberManager;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hylexus
 */
@RestController
@RequestMapping("/dashboard-api/jt1078/v1")
public class BuiltinJt1078DashboardSubscribeController {
    private final Jt1078SubscriberManager subscriberManager;

    public BuiltinJt1078DashboardSubscribeController(Jt1078SubscriberManager subscriberManager) {
        this.subscriberManager = subscriberManager;
    }

    @GetMapping("/subscribers")
    public PageableVo<Jt1078SubscriberDescriptor> subscribers(PageableDto dto) {
        final long total = this.subscriberManager.count(it -> true);
        final List<Jt1078SubscriberDescriptor> list = this.subscriberManager.list()
                .skip(dto.getOffset())
                .limit(dto.getPageSize())
                .toList();
        return PageableVo.of(total, list);
    }

    @DeleteMapping("/subscriber/{id}")
    public Map<String, Object> closeSubscriber(@PathVariable("id") String id, @RequestHeader HttpHeaders httpHeaders) {
        this.subscriberManager.closeSubscriber(id);
        return Map.of("id", id);
    }

}
