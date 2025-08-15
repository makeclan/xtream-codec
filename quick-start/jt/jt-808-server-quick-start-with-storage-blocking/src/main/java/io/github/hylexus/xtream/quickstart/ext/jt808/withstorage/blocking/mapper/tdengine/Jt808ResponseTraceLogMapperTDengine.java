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

package io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.mapper.tdengine;

import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.domain.entity.Jt808ResponseTraceLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hylexus
 */
@Mapper
public interface Jt808ResponseTraceLogMapperTDengine {

    @Insert("""
            
            
            INSERT INTO v_resp_${terminalId}
                USING jt_808_response_trace_log
                TAGS (
                #{terminalId},
                1
                ) VALUES (
               '${sentAt}',
                #{id}, #{requestId},
                #{netType}, #{traceId},  #{escapedHex}, '${createdAt}'
                ); 
            """)
    void insert(Jt808ResponseTraceLogEntity responseTraceLog);

}
