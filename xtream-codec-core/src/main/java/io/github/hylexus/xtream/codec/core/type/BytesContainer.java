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

package io.github.hylexus.xtream.codec.core.type;


import io.github.hylexus.xtream.codec.common.utils.BcdOps;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Copied from jt-framework.
 *
 * @author hylexus
 */
public interface BytesContainer {

    int length();

    byte[] asBytes();

    byte asI8();

    default short asU8() {
        return (short) (this.asI8() & 0xFF);
    }

    default short asByte() {
        return asU8();
    }

    short asI16();

    default int asU16() {
        return this.asI16() & 0xFFFF;
    }

    default int asWord() {
        return asU16();
    }

    int asI32();

    default long asDword() {
        return asU32();
    }

    default long asU32() {
        return this.asI32() & 0xFFFFFFFFL;
    }

    default String asBcd() {
        return BcdOps.decodeBcd8421AsString(this.asBytes(), 0, this.length());
    }

    String asString(Charset charset);

    default String asString() {
        return this.asString(StandardCharsets.UTF_8);
    }

}
