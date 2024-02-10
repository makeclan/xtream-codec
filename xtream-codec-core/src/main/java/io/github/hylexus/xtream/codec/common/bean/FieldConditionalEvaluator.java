package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import lombok.ToString;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public interface FieldConditionalEvaluator {

    boolean evaluateConditionalProperty(FieldCodec.FieldSerializeContext context);

    class AlwaysTrueFieldConditionalEvaluator implements FieldConditionalEvaluator {

        public static AlwaysTrueFieldConditionalEvaluator INSTANCE = new AlwaysTrueFieldConditionalEvaluator();

        @Override
        public boolean evaluateConditionalProperty(FieldCodec.FieldSerializeContext context) {
            return true;
        }
    }

    @ToString(exclude = "expression")
    class ExpressionFieldConditionalEvaluator implements FieldConditionalEvaluator {
        private final Expression expression;
        private final String expressionString;

        public ExpressionFieldConditionalEvaluator(String expressionString) {
            this.expressionString = expressionString;
            this.expression = new SpelExpressionParser().parseExpression(expressionString);
        }

        @Override
        public boolean evaluateConditionalProperty(FieldCodec.FieldSerializeContext context) {
            final Boolean value = expression.getValue(context.evaluationContext(), Boolean.class);
            return value != null && value;
        }

    }
}
