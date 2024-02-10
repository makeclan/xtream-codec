package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.FieldConditionalEvaluator;
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
import java.util.Collection;
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
    private final FieldConditionalEvaluator fieldConditionalEvaluator;
    private final XtreamField xtreamField;
    @Setter
    private FieldCodec<?> fieldCodec;

    public BasicBeanPropertyMetadata(String name, Class<?> type, Field field, PropertyGetter getter, PropertySetter setter) {
        this.name = name;
        this.type = type;
        this.field = field;
        // this.propertyDescriptor = propertyDescriptor;
        this.propertyGetter = getter;
        this.propertySetter = setter;
        this.order = initOrder();
        this.filedValueType = detectValueType(type);
        this.xtreamField = findAnnotation(XtreamField.class).orElseThrow();
        this.fieldLengthExtractor = detectFieldLengthExtractor(this.xtreamField);
        this.fieldConditionalEvaluator = detectFieldConditionalEvaluator(this.xtreamField);
    }

    private FieldConditionalEvaluator detectFieldConditionalEvaluator(XtreamField xtreamField) {
        if (XtreamUtils.hasElement(xtreamField.condition())) {
            return new FieldConditionalEvaluator.ExpressionFieldConditionalEvaluator(xtreamField.condition());
        }
        return FieldConditionalEvaluator.AlwaysTrueFieldConditionalEvaluator.INSTANCE;
    }

    protected FieldLengthExtractor detectFieldLengthExtractor(XtreamField xtreamField) {
        if (xtreamField.length() > 0) {
            return new FieldLengthExtractor.ConstantFieldLengthExtractor(xtreamField);
        }
        if (XtreamUtils.hasElement(xtreamField.lengthExpression())) {
            return new FieldLengthExtractor.ExpressionFieldLengthExtractor(xtreamField);
        }

        return XtreamTypes.getDefaultSizeInBytes(this.rawClass())
                .map(FieldLengthExtractor.ConstantFieldLengthExtractor::new)
                .map(FieldLengthExtractor.class::cast)
                .orElseGet(() -> new FieldLengthExtractor.PlaceholderFieldLengthExtractor("Did you forget to specify length() / lengthExpression() for Field: [ " + this.field + " ]"));
    }

    protected FiledDataType detectValueType(Class<?> type) {
        return this.findAnnotation(XtreamField.class)
                .map(XtreamField::dataType)
                .filter(it -> it != FiledDataType.unknown)
                .orElseGet(() -> {
                    if (XtreamTypes.isBasicType(type)) {
                        return FiledDataType.basic;
                    }
                    if (Collection.class.isAssignableFrom(type)) {
                        return FiledDataType.sequence;
                    }
                    return FiledDataType.nested;
                });
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

    // @Override
    // public PropertyDescriptor descriptor() {
    //     return this.propertyDescriptor;
    // }

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
    public FieldConditionalEvaluator conditionEvaluator() {
        return this.fieldConditionalEvaluator;
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
        return fieldCodec().deserialize(context, input, length);
    }

    @Override
    public void encodePropertyValue(FieldCodec.FieldSerializeContext context, ByteBuf output, Object value) {
        FieldCodec<Object> codec = (FieldCodec<Object>) fieldCodec();
        codec.serialize(context, output, value);
    }

    @Override
    public String toString() {
        return "SimpleBeanPropertyMetadata{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", filedValueType=" + filedValueType +
                ", field=" + field +
                ", fieldCodec=" + fieldCodec +
                ", propertyGetter=" + propertyGetter +
                ", propertySetter=" + propertySetter +
                ", order=" + order +
                ", fieldLengthExtractor=" + fieldLengthExtractor +
                ", xtreamField=" + xtreamField +
                '}';
    }

}
