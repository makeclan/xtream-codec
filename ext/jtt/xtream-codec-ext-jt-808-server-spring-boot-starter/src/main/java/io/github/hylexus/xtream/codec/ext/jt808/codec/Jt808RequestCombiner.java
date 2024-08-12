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

package io.github.hylexus.xtream.codec.ext.jt808.codec;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import jakarta.annotation.Nullable;

/**
 * 分包请求合并
 *
 * @author hylexus
 */
public interface Jt808RequestCombiner {

    @Nullable
    Jt808Request tryMergeSubPackage(Jt808Request jt808Request);

}
