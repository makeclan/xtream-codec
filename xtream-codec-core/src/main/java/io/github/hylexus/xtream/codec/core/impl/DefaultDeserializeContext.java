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
