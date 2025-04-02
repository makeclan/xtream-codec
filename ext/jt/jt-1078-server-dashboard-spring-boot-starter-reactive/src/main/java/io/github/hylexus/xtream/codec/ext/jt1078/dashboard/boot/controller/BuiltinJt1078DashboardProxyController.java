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

package io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.controller;

import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.Jt808SessionQueryDto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.command.DashboardCommand9101Dto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.command.DashboardCommand9102Dto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.vo.Jt808SessionVo;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.vo.PageableVo;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.service.Jt808ProxyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dashboard-api/jt1078/808-server-proxy/v1/")
public class BuiltinJt1078DashboardProxyController {

    private final Jt808ProxyService jt808ProxyService;

    public BuiltinJt1078DashboardProxyController(Jt808ProxyService jt808ProxyService) {
        this.jt808ProxyService = jt808ProxyService;
    }

    @GetMapping("/session/instruction-sessions")
    public Mono<PageableVo<Jt808SessionVo>> instructionSessionList(Jt808SessionQueryDto dto) {
        return jt808ProxyService.instructionSessionList(dto);
    }

    @PostMapping("/command/9102")
    public Mono<Object> command9102(@Validated @RequestBody DashboardCommand9102Dto dto) {
        return this.jt808ProxyService.command9102(dto);
    }

    @PostMapping("/command/9101")
    public Mono<Object> command9101(@Validated @RequestBody DashboardCommand9101Dto dto) {
        return this.jt808ProxyService.command9101(dto);
    }

}
