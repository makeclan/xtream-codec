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

package io.github.hylexus.xtream.debug.codec.core.democustomannotation;

import io.github.hylexus.xtream.codec.core.type.Preset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
// 实现 DebugEntity01 仅仅是为了方便做单元测试，没其他特殊意义
public class CustomAnnotationDebugEntity01 {

    // 主版本号 无符号数 1字节
    @Preset.RustStyle.u8
    private short majorVersion = 2;
    // 次版本号 无符号数 1字节

    // 生日 String[8], "yyyyMMdd", "UTF-8" 8字节
    @MyDateType
    private LocalDate birthday;

    // ... 其他属性 ...
}
