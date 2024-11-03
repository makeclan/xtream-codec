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

package io.github.hylexus.xtream.debug.ext.jt808.controller;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808SessionManager;
import io.github.hylexus.xtream.debug.ext.jt808.domain.dto.Jt808SessionQueryDto;
import io.github.hylexus.xtream.debug.ext.jt808.domain.vo.Jt808SessionVo;
import io.github.hylexus.xtream.debug.ext.jt808.domain.vo.PageableVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author hylexus
 */
@RestController
@RequestMapping("/api/v1")
public class DemoJt808SessionController {
    private final Jt808SessionManager jt808SessionManager;

    public DemoJt808SessionController(Jt808SessionManager jt808SessionManager) {
        this.jt808SessionManager = jt808SessionManager;
    }

    @GetMapping("/instruction-session/list")
    public PageableVo<Jt808SessionVo> list(Jt808SessionQueryDto dto) {
        final Predicate<Jt808Session> filter = createFilter(dto);
        final long total = this.jt808SessionManager.count(filter);
        if (total <= 0) {
            return PageableVo.empty();
        }
        final List<Jt808SessionVo> list = jt808SessionManager
                .list(dto.getPage(), dto.getSize(), filter, this::convertToVo)
                .toList();
        return PageableVo.of(total, list);
    }

    Predicate<Jt808Session> createFilter(Jt808SessionQueryDto dto) {
        Predicate<Jt808Session> predicate = it -> true;
        if (dto.getTerminalId() != null) {
            predicate = predicate.and(it -> it.terminalId().equals(dto.getTerminalId()));
        }
        if (dto.getServerType() != null) {
            predicate = predicate.and(it -> it.role() == dto.getServerType());
        }
        if (dto.getProtocolVersion() != null) {
            predicate = predicate.and(it -> it.protocolVersion() == dto.getProtocolVersion());
        }
        if (dto.getProtocolType() != null) {
            predicate = predicate.and(it -> it.type() == dto.getProtocolType());
        }
        return predicate;
    }

    Jt808SessionVo convertToVo(Jt808Session it) {
        return new Jt808SessionVo()
                .setId(it.id())
                .setTerminalId(it.terminalId())
                .setServerType(it.role())
                .setProtocolVersion(it.protocolVersion())
                .setProtocolType(it.type())
                .setCreationTime(it.creationTime())
                .setLastCommunicateTime(it.lastCommunicateTime())
                ;
    }
}
