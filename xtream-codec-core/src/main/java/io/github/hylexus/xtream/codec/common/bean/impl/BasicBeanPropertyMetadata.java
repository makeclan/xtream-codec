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

package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.FieldConditionEvaluator;
import io.github.hylexus.xtream.codec.common.bean.FieldLengthExtractor;
import io.github.hylexus.xtream.codec.common.utils.XtreamTypes;
import io.github.hylexus.xtream.codec.common.utils.XtreamUtils;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import io.netty.buffer.ByteBuf;
import lombok.Setter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

public class BasicBeanPropertyMetadata implements BeanPropertyMetadata {

    private final String name;
    private final Class<?> type;
    private final FiledDataType filedValueType;
    private final Field field;
    // private final PropertyDescriptor propertyDescriptor;
    private final PropertyGetter propertyGetter;
    private final PropertySetter propertySetter;
    private final int order;
    private final FieldLengthExtractor fieldLengthExtractor;
    private final FieldConditionEvaluator fieldConditionEvaluator;
    protected final XtreamField xtreamField;
    @Setter
    private FieldCodec<?> fieldCodec;

    public BasicBeanPropertyMetadata(String name, Class<?> type, Field field, PropertyGetter getter, PropertySetter setter) {
        this.name = name;
        this.type = type;
        this.field = field;
        this.propertyGetter = getter;
        this.propertySetter = setter;
        this.order = initOrder();
        this.filedValueType = XtreamTypes.detectFieldDataType(field);
        this.xtreamField = findAnnotation(XtreamField.class).orElseThrow();
        this.fieldLengthExtractor = detectFieldLengthExtractor(this.xtreamField);
        this.fieldConditionEvaluator = detectFieldConditionalEvaluator(this.xtreamField);
    }

    private FieldConditionEvaluator detectFieldConditionalEvaluator(XtreamField xtreamField) {
        if (XtreamUtils.hasElement(xtreamField.condition())) {
            return new FieldConditionEvaluator.ExpressionFieldConditionEvaluator(xtreamField.condition());
        }
        return FieldConditionEvaluator.AlwaysTrueFieldConditionEvaluator.INSTANCE;
    }

    /**
     * @see FieldCodec#deserialize(BeanPropertyMetadata, FieldCodec.DeserializeContext, ByteBuf, int) StringFieldCodec#deserialize
     * @see SequenceBeanPropertyMetadata#decodePropertyValue(FieldCodec.DeserializeContext, ByteBuf) SequenceBeanPropertyMetadata#decodePropertyValue
     * @see NestedBeanPropertyMetadata#decodePropertyValue(FieldCodec.DeserializeContext, ByteBuf) NestedBeanPropertyMetadata#decodePropertyValue
     * @see MapBeanPropertyMetadata#decodePropertyValue(FieldCodec.DeserializeContext, ByteBuf) MapBeanPropertyMetadata#decodePropertyValue
     */
    protected FieldLengthExtractor detectFieldLengthExtractor(XtreamField xtreamField) {
        if (xtreamField.length() > 0) {
            return new FieldLengthExtractor.ConstantFieldLengthExtractor(xtreamField.length());
        }

        if (XtreamUtils.hasElement(xtreamField.lengthExpression())) {
            return new FieldLengthExtractor.ExpressionFieldLengthExtractor(xtreamField);
        }

        return XtreamTypes.getDefaultSizeInBytes(this.rawClass())
                .map(FieldLengthExtractor.ConstantFieldLengthExtractor::new)
                .map(FieldLengthExtractor.class::cast)
                .orElseGet(() -> {
                    final FiledDataType filedDataType = XtreamTypes.detectFieldDataType(this.rawClass());
                    return switch (filedDataType) {
                        case sequence, struct, map -> new FieldLengthExtractor.ConstantFieldLengthExtractor(-2);
                        case basic -> {
                            // String 类型: 没指定长度 ==> 读取剩余所有字节
                            if (this.rawClass() == String.class) {
                                yield new FieldLengthExtractor.ConstantFieldLengthExtractor(-2);
                            }
                            yield this.placeholderFieldLengthExtractor();
                        }
                        default -> this.placeholderFieldLengthExtractor();
                    };
                });
    }

    private FieldLengthExtractor.PlaceholderFieldLengthExtractor placeholderFieldLengthExtractor() {
        return new FieldLengthExtractor.PlaceholderFieldLengthExtractor("Did you forget to specify length() / lengthExpression() for Field: [ " + this.field + " ]");
    }

    protected int initOrder() {
        return this.findAnnotation(XtreamField.class).map(XtreamField::order).orElse(0);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<?> rawClass() {
        return this.type;
    }

    @Override
    public FiledDataType dataType() {
        return this.filedValueType;
    }

    @Override
    public Field field() {
        return this.field;
    }

    @Override
    public XtreamField xtreamFieldAnnotation() {
        return this.xtreamField;
    }

    @Override
    public PropertyGetter propertyGetter() {
        return this.propertyGetter;
    }

    @Override
    public PropertySetter propertySetter() {
        return this.propertySetter;
    }

    @Override
    public FieldCodec<?> fieldCodec() {
        return this.fieldCodec;
    }

    @Override
    public FieldLengthExtractor fieldLengthExtractor() {
        return this.fieldLengthExtractor;
    }

    @Override
    public FieldConditionEvaluator conditionEvaluator() {
        return this.fieldConditionEvaluator;
    }

    @Override
    public int order() {
        return this.order;
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationClass) {
        // todo cache...
        final A mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(this.field(), annotationClass);
        return Optional.ofNullable(mergedAnnotation);
    }

    public Object decodePropertyValue(FieldCodec.DeserializeContext context, ByteBuf input) {
        final int length = this.fieldLengthExtractor.extractFieldLength(context, context.evaluationContext());
        return fieldCodec().deserialize(this, context, input, length);
    }

    @Override
    public void encodePropertyValue(FieldCodec.SerializeContext context, ByteBuf output, Object value) {
        @SuppressWarnings("unchecked") final FieldCodec<Object> codec = (FieldCodec<Object>) fieldCodec();
        codec.serialize(this, context, output, value);
    }

    @Override
    public String toString() {
        return "SimpleBeanPropertyMetadata{"
                + "name='" + name + '\''
                + ", type=" + type
                + ", filedValueType=" + filedValueType
                + ", field=" + field
                + ", fieldCodec=" + fieldCodec
                + ", propertyGetter=" + propertyGetter
                + ", propertySetter=" + propertySetter
                + ", order=" + order
                + ", fieldLengthExtractor=" + fieldLengthExtractor
                + ", xtreamField=" + xtreamField
                + '}';
    }

}
