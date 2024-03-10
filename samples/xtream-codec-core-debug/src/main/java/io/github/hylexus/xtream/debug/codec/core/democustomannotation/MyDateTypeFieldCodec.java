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

import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDateTypeFieldCodec implements FieldCodec<LocalDate> {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public LocalDate deserialize(DeserializeContext context, ByteBuf input, int length) {
        // yyyyMMdd
        final CharSequence dateString = input.readCharSequence(8, StandardCharsets.UTF_8);
        return LocalDate.parse(dateString, formatter);
    }

    @Override
    public void serialize(SerializeContext context, ByteBuf output, LocalDate value) {
        final String dateString = value.format(formatter);
        output.writeCharSequence(dateString, StandardCharsets.UTF_8);
    }
}
