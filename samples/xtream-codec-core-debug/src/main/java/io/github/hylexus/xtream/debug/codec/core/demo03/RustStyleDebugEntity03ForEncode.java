/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.debug.codec.core.demo03;

import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.github.hylexus.xtream.codec.core.annotation.XtreamFieldMapDescriptor;
import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class RustStyleDebugEntity03ForEncode {

    @Preset.RustStyle.u16
    private int msgId;

    @Preset.RustStyle.map
    @XtreamFieldMapDescriptor(
            keyDescriptor = @XtreamFieldMapDescriptor.KeyDescriptor(type = XtreamFieldMapDescriptor.KeyType.u16),
            valueLengthFieldDescriptor = @XtreamFieldMapDescriptor.ValueLengthFieldDescriptor(length = 1, littleEndian = false),
            valueEncoderDescriptors = @XtreamFieldMapDescriptor.ValueEncoderDescriptors(
                    defaultValueEncoderDescriptor = @XtreamFieldMapDescriptor.ValueEncoderDescriptor(
                            config = @XtreamField(charset = "utf-8")
                    ),
                    valueEncoderDescriptors = {
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU16 = 1, javaType = String.class),
                            @XtreamFieldMapDescriptor.ValueCodecConfig(whenKeyIsU16 = 2, javaType = String.class, config = @XtreamField(charset = "gbk"), valueLengthFieldSize = 2),
                    }
            )
    )
    private Map<Integer, Object> attr;
}
