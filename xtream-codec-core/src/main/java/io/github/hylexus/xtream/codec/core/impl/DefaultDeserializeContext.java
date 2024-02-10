package io.github.hylexus.xtream.codec.core.impl;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class DefaultDeserializeContext implements FieldCodec.DeserializeContext {

    private final Object containerInstance;
    private final EvaluationContext evaluationContext;

    public DefaultDeserializeContext(Object containerInstance) {
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
}