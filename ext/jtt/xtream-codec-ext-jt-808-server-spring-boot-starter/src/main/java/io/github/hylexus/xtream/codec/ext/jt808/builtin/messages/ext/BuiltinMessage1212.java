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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BuiltinMessage1212 {

    // byte[0]
    @Preset.JtStyle.Byte
    private short fileNameLength;

    @Preset.JtStyle.Str(lengthExpression = "getFileNameLength()")
    private String fileName;

    @Preset.JtStyle.Byte
    private short fileType;

    @Preset.JtStyle.Dword
    private long fileSize;
}
