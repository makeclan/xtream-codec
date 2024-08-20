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
