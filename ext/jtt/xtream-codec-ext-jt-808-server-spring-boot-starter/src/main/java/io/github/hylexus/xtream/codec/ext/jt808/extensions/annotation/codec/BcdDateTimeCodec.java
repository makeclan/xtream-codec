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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.annotation.codec;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.impl.codec.AbstractFieldCodec;
import io.github.hylexus.xtream.codec.core.impl.codec.StringFieldCodec;
import io.github.hylexus.xtream.codec.ext.jt808.utils.JtProtocolConstant;
import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author hylexus
 */
public class BcdDateTimeCodec extends AbstractFieldCodec<Object> {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT);
    private final FieldCodec<String> delegate;

    public BcdDateTimeCodec() {
        this.delegate = StringFieldCodec.initDelegateCodec("bcd_8421");
    }

    @Override
    protected void doSerialize(SerializeContext context, ByteBuf output, Object value) {
        final String bcdString = switch (value) {
            case LocalDateTime localDateTime -> DATE_TIME_FORMATTER.format(localDateTime);
            case Date date -> new SimpleDateFormat(JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT).format(date);
            case String string -> string;
            default -> throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
        };
        this.delegate.serialize(context, output, bcdString);
    }

    @Override
    public Object deserialize(DeserializeContext context, ByteBuf input, int length) {
        final String bcdString = this.delegate.deserialize(context, input, length);
        // todo 支持其他日期类型
        return LocalDateTime.parse(bcdString, DATE_TIME_FORMATTER);
    }
}
