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

package io.github.hylexus.xtream.codec.server.reactive.spec.impl;

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;

public class DefaultXtreamRequestBuilder
        extends AbstractXtreamRequestBuilder<XtreamRequest.XtreamRequestBuilder, XtreamRequest> {

    public DefaultXtreamRequestBuilder(XtreamRequest delegate) {
        super(delegate);
        this.remoteAddress = delegate.remoteAddress();
    }

    public XtreamRequest build() {
        if (this.delegateRequest.type() == XtreamRequest.Type.TCP) {
            return new DefaultXtreamRequest(
                    this.delegateRequest.bufferFactory(),
                    this.delegateRequest.underlyingInbound(),
                    this.payload != null ? this.payload : this.delegateRequest.payload()
            );
        }
        return new DefaultXtreamRequest(
                this.delegateRequest.bufferFactory(),
                this.delegateRequest.underlyingInbound(),
                this.createDatagramPacket(this.payload)
        );
    }

}
