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

package io.github.hylexus.xtream.codec.core.impl;

import io.github.hylexus.xtream.codec.core.EntityDecoder;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class DefaultDeserializeContext implements FieldCodec.DeserializeContext {

    private final EntityDecoder entityDecoder;
    private final Object containerInstance;
    private final EvaluationContext evaluationContext;

    public DefaultDeserializeContext(FieldCodec.DeserializeContext another, Object containerInstance) {
        this(another.entityDecoder(), containerInstance);
    }

    public DefaultDeserializeContext(EntityDecoder entityDecoder, Object containerInstance) {
        this.entityDecoder = entityDecoder;
        this.containerInstance = containerInstance;
        this.evaluationContext = new StandardEvaluationContext(containerInstance);
    }

    @Override
    public Object containerInstance() {
        return this.containerInstance;
    }

    @Override
    public EvaluationContext evaluationContext() {
        return this.evaluationContext;
    }

    @Override
    public EntityDecoder entityDecoder() {
        return this.entityDecoder;
    }
}
