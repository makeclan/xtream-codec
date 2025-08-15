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

import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.domain.dto.Jt808AlarmAttachmentInfoDto;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.domain.entity.Jt808AlarmAttachmentInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hylexus
 */
@Mapper
public interface Jt808AlarmAttachmentInfoMapperTDengine {

    @Insert("""
                       INSERT INTO v_attach_${terminalId}
            USING jt_808_alarm_attachment_info
            TAGS (
            #{terminalId},
            1
            ) VALUES (
                 '${alarmTime}',
            #{id},#{alarmNo}, #{attachmentCount}, #{fileName}, #{fileType}, #{fileSize}, #{filePath},
                        #{alarmSequence}, #{clientId},
                        '${createdAt}'
            )         
            """)
    void insert(Jt808AlarmAttachmentInfoEntity entity);

    long count(@Param("dto") Jt808AlarmAttachmentInfoDto dto);

    List<Jt808AlarmAttachmentInfoEntity> list(@Param("dto") Jt808AlarmAttachmentInfoDto dto);
}
