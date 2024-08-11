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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchange;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamExchangeDecorator;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamRequest;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;

import java.util.function.Consumer;

public class DefaultXtreamExchangeBuilder implements XtreamExchange.XtreamExchangeBuilder {
    protected final XtreamExchange delegate;
    protected XtreamRequest request;
    protected XtreamResponse response;

    public DefaultXtreamExchangeBuilder(XtreamExchange delegate) {
        this.delegate = delegate;
        this.request = delegate.request();
        this.response = delegate.response();
    }

    @Override
    public XtreamExchange.XtreamExchangeBuilder request(XtreamRequest request) {
        this.request = request;
        return this;
    }

    @Override
    public XtreamExchange.XtreamExchangeBuilder request(Consumer<XtreamRequest.XtreamRequestBuilder> requestBuilder) {
        final XtreamRequest.XtreamRequestBuilder builder = this.delegate.request().mutate();
        requestBuilder.accept(builder);
        final XtreamRequest newRequest = builder.build();
        return this.request(newRequest);
    }

    @Override
    public XtreamExchange.XtreamExchangeBuilder response(XtreamResponse response) {
        this.response = response;
        return this;
    }

    @Override
    public XtreamExchange build() {
        return new XtreamExchangeDecorator(this.delegate, this.request, this.response);
    }

}
