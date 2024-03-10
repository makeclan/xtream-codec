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

package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.FieldCodec;
import lombok.ToString;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public interface FieldConditionEvaluator {

    boolean evaluate(FieldCodec.CodecContext context);

    class AlwaysTrueFieldConditionEvaluator implements FieldConditionEvaluator {

        public static AlwaysTrueFieldConditionEvaluator INSTANCE = new AlwaysTrueFieldConditionEvaluator();

        @Override
        public boolean evaluate(FieldCodec.CodecContext context) {
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
        public boolean evaluate(FieldCodec.CodecContext context) {
            final Boolean value = expression.getValue(context.evaluationContext(), Boolean.class);
            return value != null && value;
        }

    }
}
