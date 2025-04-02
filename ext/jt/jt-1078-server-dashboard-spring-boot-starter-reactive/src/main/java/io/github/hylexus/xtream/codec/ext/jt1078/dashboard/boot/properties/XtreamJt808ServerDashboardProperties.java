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

package io.github.hylexus.xtream.codec.ext.jt1078.dashboard.boot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jt1078-server.features.dashboard")
public class XtreamJt808ServerDashboardProperties {

    private boolean enabled = true;
    // 暂时不支持动态配置
    private final String basePath = "/dashboard-ui/";
    /**
     * 将 `/` 307 到 `/dashboard-ui/`
     */
    private boolean redirectRootToBasePath = true;
    /**
     * 将 `base-path` 开头的 404 请求转发到 `index.html`
     * <p>
     * 如果不开启这个配置，刷新 dashboard 页面时会出现404(也就是说只能刷新 `base-path` 根路由，其他子路由刷新会 404)
     */
    private boolean forwardNotFoundToIndex = true;

    @NestedConfigurationProperty
    private CodecConfig codecDebugOptions = new CodecConfig();

    public String getFormatedBasePath() {
        return formatBasePath(this.getBasePath());
    }

    @Getter
    @Setter
    public static class CodecConfig {
        private static final String DEFAULT_PACKAGE = "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages";

        private List<String> basePackages = List.of(DEFAULT_PACKAGE);
        private String defaultTerminalId = "00000000013912344329";

        public void setBasePackages(List<String> basePackages) {
            if (CollectionUtils.isEmpty(basePackages)) {
                this.basePackages.add(DEFAULT_PACKAGE);
            } else {
                if (!basePackages.contains(DEFAULT_PACKAGE)) {
                    basePackages.add(DEFAULT_PACKAGE);
                }
                this.basePackages = basePackages;
            }
        }

    }

    public static String formatBasePath(String input) {
        if (!StringUtils.hasText(input)) {
            return "/dashboard-ui/";
        }
        input = input.trim();
        if (!input.startsWith("/")) {
            input = "/" + input;
        }
        if (!input.endsWith("/")) {
            input += "/";
        }
        return input;
    }
}
