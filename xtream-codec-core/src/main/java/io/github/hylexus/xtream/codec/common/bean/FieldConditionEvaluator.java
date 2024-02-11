package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import lombok.ToString;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public interface FieldConditionEvaluator {

    boolean evaluate(FieldCodec.SerializeContext context);

    class AlwaysTrueFieldConditionEvaluator implements FieldConditionEvaluator {

        public static AlwaysTrueFieldConditionEvaluator INSTANCE = new AlwaysTrueFieldConditionEvaluator();

        @Override
        public boolean evaluate(FieldCodec.SerializeContext context) {
            return true;
        }
    }

    @ToString(exclude = "expression")
    class ExpressionFieldConditionEvaluator implements FieldConditionEvaluator {
        private final Expression expression;
        private final String expressionString;

        public ExpressionFieldConditionEvaluator(String expressionString) {
            this.expressionString = expressionString;
            this.expression = new SpelExpressionParser().parseExpression(expressionString);
        }

        @Override
        public boolean evaluate(FieldCodec.SerializeContext context) {
            final Boolean value = expression.getValue(context.evaluationContext(), Boolean.class);
            return value != null && value;
        }

    }
}
