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

package io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.service.service;

import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.Jt808SessionQueryDto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.command.DashboardCommand9101Dto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.dto.command.DashboardCommand9102Dto;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.vo.Jt808SessionVo;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.domain.vo.PageableVo;
import io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.service.Jt808ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

public class DefaultJt808ProxyService implements Jt808ProxyService {

    private static final Logger log = LoggerFactory.getLogger(DefaultJt808ProxyService.class);
    private final WebClient webClient;

    public DefaultJt808ProxyService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8888")
                .filter(((request, next) -> {
                    log.info("Proxy Request to: {}", request.url());
                    return next.exchange(request);
                }))
                .build();
    }

    @Override
    public Mono<PageableVo<Jt808SessionVo>> instructionSessionList(Jt808SessionQueryDto dto) {
        return proxyGetRequest(
                uriBuilder -> {
                    uriBuilder.path("/dashboard-api/jt808/v1/session/instruction-sessions");
                    uriBuilder
                            .queryParamIfPresent("page", Optional.ofNullable(dto.getPage()))
                            .queryParamIfPresent("pageSize", Optional.ofNullable(dto.getPageSize()))
                            .queryParamIfPresent("terminalId", Optional.ofNullable(dto.getTerminalId()))
                            .queryParamIfPresent("serverType", Optional.ofNullable(dto.getServerType()))
                            .queryParamIfPresent("protocolVersion", Optional.ofNullable(dto.getProtocolVersion()))
                            .queryParamIfPresent("protocolType", Optional.ofNullable(dto.getProtocolType()).map(Enum::name))
                    ;
                    return uriBuilder.build();
                },
                new ParameterizedTypeReference<PageableVo<Jt808SessionVo>>() {
                }
        );
    }

    @Override
    public Mono<Object> command9102(DashboardCommand9102Dto dto) {
        return proxyJsonRequest(
                uriBuilder -> uriBuilder.path("/dashboard-api/jt808/v1/command/9102").build(),
                dto,
                new ParameterizedTypeReference<Object>() {
                }
        );
    }

    @Override
    public Mono<Object> command9101(DashboardCommand9101Dto dto) {
        return proxyJsonRequest(
                uriBuilder -> uriBuilder.path("/dashboard-api/jt808/v1/command/9101").build(),
                dto, new ParameterizedTypeReference<Object>() {
                }
        );
    }

    private <T> Mono<T> proxyGetRequest(Function<UriBuilder, URI> uriFunction, ParameterizedTypeReference<T> elementTypeRef) {
        return this.webClient.mutate().build()
                .get()
                .uri(uriFunction)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(elementTypeRef);
                    } else {
                        return response.createError();
                    }
                });
    }

    private <T> Mono<T> proxyJsonRequest(Function<UriBuilder, URI> uriFunction, Object dto, ParameterizedTypeReference<T> elementTypeRef) {
        return this.webClient.mutate().build()
                .post()
                .uri(uriFunction)
                .bodyValue(dto)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(elementTypeRef);
                    } else {
                        return response.createError();
                    }
                });
    }

}
