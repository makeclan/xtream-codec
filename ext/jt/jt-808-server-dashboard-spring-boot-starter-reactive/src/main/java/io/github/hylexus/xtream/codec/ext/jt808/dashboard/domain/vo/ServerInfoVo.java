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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.SimpleTypes;

import java.time.Instant;

/**
 * @author hylexus
 */
public record ServerInfoVo(
        String xtreamCodecVersion,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8") Instant serverStartupTime,
        SimpleTypes.JavaInfo java,
        SimpleTypes.OsInfo os,
        XtreamJt808ServerProperties configuration) {

    public ServerInfoVo(String xtreamCodecVersion, Instant serverStartupTime, XtreamJt808ServerProperties configuration) {
        this(xtreamCodecVersion, serverStartupTime, SimpleTypes.JavaInfo.INSTANCE, SimpleTypes.OsInfo.INSTANCE, configuration);
    }

}
