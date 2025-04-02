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

package io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.service;

import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.Jt808SessionQueryDto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.command.DashboardCommand9101Dto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.command.DashboardCommand9102Dto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.vo.Jt808SessionVo;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.vo.PageableVo;
import reactor.core.publisher.Mono;

public interface Jt808ProxyService {

    Mono<PageableVo<Jt808SessionVo>> instructionSessionList(Jt808SessionQueryDto dto);

    Mono<Object> command9102(DashboardCommand9102Dto dto);

    Mono<Object> command9101(DashboardCommand9101Dto dto);

}
