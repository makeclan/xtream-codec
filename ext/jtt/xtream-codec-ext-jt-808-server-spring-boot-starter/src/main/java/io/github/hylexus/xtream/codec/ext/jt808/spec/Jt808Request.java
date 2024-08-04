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

package io.github.hylexus.xtream.codec.ext.jt808.spec;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.netty.buffer.ByteBuf;

public interface Jt808Request extends XtreamRequest {

    Jt808RequestHeader header();

    ByteBuf body();

    int calculatedCheckSum();

    int originalCheckSum();

    interface Jt808RequestBuilder extends XtreamRequestBuilder {

        Jt808RequestBuilder header(Jt808RequestHeader header);

        Jt808RequestBuilder body(ByteBuf body, boolean autoRelease);

        default Jt808RequestBuilder body(ByteBuf body) {
            return this.body(body, true);
        }

        Jt808RequestBuilder calculatedCheckSum(Integer calculatedCheckSum);

        Jt808RequestBuilder originalCheckSum(Integer originalCheckSum);

    }
}
