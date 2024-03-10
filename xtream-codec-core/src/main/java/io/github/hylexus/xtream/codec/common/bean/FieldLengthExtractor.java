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
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import lombok.ToString;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public interface FieldLengthExtractor {

    int extractFieldLength(FieldCodec.DeserializeContext context, EvaluationContext evaluationContext);

    @ToString
    class ConstantFieldLengthExtractor implements FieldLengthExtractor {
        private final int length;

        public ConstantFieldLengthExtractor(int length) {
            this.length = length;
        }

        @Override
        public int extractFieldLength(FieldCodec.DeserializeContext context, EvaluationContext evaluationContext) {
            return this.length;
        }
    }

    @ToString(exclude = "expression")
    class ExpressionFieldLengthExtractor implements FieldLengthExtractor {
        final Expression expression;
        private final String expressionString;

        public ExpressionFieldLengthExtractor(XtreamField field) {
            this.expressionString = field.lengthExpression();
            this.expression = new SpelExpressionParser().parseExpression(expressionString);
        }

        @Override
        public int extractFieldLength(FieldCodec.DeserializeContext context, EvaluationContext evaluationContext) {
            final Number number = expression.getValue(evaluationContext, Number.class);
            if (number == null) {
                throw new IllegalArgumentException("Can not determine field length with Expression[" + expressionString + "]");
            }
            return number.intValue();
        }
    }

    class PlaceholderFieldLengthExtractor implements FieldLengthExtractor {
        private final String msg;

        public PlaceholderFieldLengthExtractor(String msg) {
            this.msg = msg;
        }

        @Override
        public int extractFieldLength(FieldCodec.DeserializeContext context, EvaluationContext evaluationContext) {
            throw new IllegalArgumentException(msg);
        }
    }

}
