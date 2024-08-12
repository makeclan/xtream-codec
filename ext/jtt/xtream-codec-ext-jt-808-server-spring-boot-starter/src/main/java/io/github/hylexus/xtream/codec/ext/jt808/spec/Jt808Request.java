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

    default ByteBuf body() {
        return this.payload();
    }

    int calculatedCheckSum();

    int originalCheckSum();

    @Override
    Jt808RequestBuilder mutate();

    interface Jt808RequestBuilder extends XtreamRequestBuilder {

        Jt808RequestBuilder header(Jt808RequestHeader header);

        @Override
        default Jt808RequestBuilder payload(ByteBuf payload) {
            return this.payload(payload, true);
        }

        @Override
        Jt808RequestBuilder payload(ByteBuf payload, boolean autoRelease);

        default Jt808RequestBuilder body(ByteBuf body, boolean autoRelease) {
            return this.payload(body, autoRelease);
        }

        default Jt808RequestBuilder body(ByteBuf body) {
            return this.payload(body);
        }

        Jt808RequestBuilder calculatedCheckSum(Integer calculatedCheckSum);

        Jt808RequestBuilder originalCheckSum(Integer originalCheckSum);

        @Override
        Jt808Request build();
    }
}
