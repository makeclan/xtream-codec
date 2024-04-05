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

package io.github.hylexus.xtream.codec.common.bean.impl;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.utils.XtreamTypes;
import io.github.hylexus.xtream.codec.core.BeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.ContainerInstanceFactory;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.FieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.annotation.XtreamFieldMapDescriptor;
import io.github.hylexus.xtream.codec.core.impl.codec.DelegateBeanMetadataFieldCodec;
import io.github.hylexus.xtream.codec.core.utils.BeanUtils;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

public class MapBeanPropertyMetadata extends BasicBeanPropertyMetadata {

    final BeanPropertyMetadata delegate;
    final XtreamFieldMapDescriptor xtreamFieldMapDescriptor;
    final FieldCodecRegistry fieldCodecRegistry;
    private final BeanMetadataRegistry beanMetadataRegistry;

    final FieldCodec<?> keyFieldCodec;
    final FieldCodec<?> defaultValueLengthSizeFieldCodec;
    final FieldCodec<?> defaultValueFieldCodec;
    private final Map<Object, FieldCodec<?>> specialValueFieldCodec;
    private final Map<Object, FieldCodec<?>> valueLengthSizeFieldCodec;
    private final ParsedMapItemMetadata parsedMapItemMetadata;
    private final ContainerInstanceFactory containerInstanceFactory;

    public MapBeanPropertyMetadata(BeanPropertyMetadata delegate, FieldCodecRegistry fieldCodecRegistry, BeanMetadataRegistry beanMetadataRegistry) {
        super(delegate.name(), delegate.rawClass(), delegate.field(), delegate.propertyGetter(), delegate.propertySetter());
        this.delegate = delegate;
        this.fieldCodecRegistry = fieldCodecRegistry;
        this.beanMetadataRegistry = beanMetadataRegistry;
        this.xtreamFieldMapDescriptor = this.findAnnotation(XtreamFieldMapDescriptor.class).orElseThrow();

        this.parsedMapItemMetadata = this.parseMapItemMetadata(xtreamFieldMapDescriptor);
        this.keyFieldCodec = this.detectKeyFieldCodec(xtreamFieldMapDescriptor, parsedMapItemMetadata);
        this.specialValueFieldCodec = this.initValueFieldCodec(parsedMapItemMetadata);
        this.valueLengthSizeFieldCodec = this.initValueLengthSizeFieldCodec(parsedMapItemMetadata);
        this.defaultValueLengthSizeFieldCodec = this.detectDefaultValueLengthSizeFieldCodec(xtreamFieldMapDescriptor, parsedMapItemMetadata);
        this.defaultValueFieldCodec = this.initDefaultValueFieldCodec(parsedMapItemMetadata);
        this.containerInstanceFactory = super.xtreamField.containerInstanceFactoryClass() == ContainerInstanceFactory.PlaceholderContainerInstanceFactory.class
                ? BeanUtils.createNewInstance(ContainerInstanceFactory.LinkedHashMapContainerInstanceFactory.class, (Object[]) null)
                : BeanUtils.createNewInstance(this.xtreamField.containerInstanceFactoryClass(), (Object[]) null);
    }

    @Override
    public ContainerInstanceFactory containerInstanceFactory() {
        return this.containerInstanceFactory;
    }

    @Override
    public Object decodePropertyValue(FieldCodec.DeserializeContext context, ByteBuf input) {
        final int length = delegate.fieldLengthExtractor().extractFieldLength(context, context.evaluationContext());
        final ByteBuf slice = length < 0
                ? input // all remaining
                : input.readSlice(length);
        @SuppressWarnings({"unchecked"}) final Map<Object, Object> map = (Map<Object, Object>) this.containerInstanceFactory().create();
        while (slice.isReadable()) {
            // 1. key(i8,u8,i16,u16,i32,u32,i64,string)
            final Object key = this.keyFieldCodec.deserialize(context, slice, this.xtreamFieldMapDescriptor.keyDescriptor().length());
            // 2. valueLength(int)
            final FieldCodec<?> valueLengthFieldCodec = this.getValueLengthFieldCodec(key);
            final int valueLength = ((Number) valueLengthFieldCodec.deserialize(context, slice, this.parsedMapItemMetadata.defaultValueItemMetadata().getLength())).intValue();
            // 3. value(dynamic)
            final ByteBuf byteBuf = slice.readSlice(valueLength);
            final FieldCodec<?> valueFieldCodec = this.getValueFieldCodec(key);
            final Object value = valueFieldCodec.deserialize(context, byteBuf, valueLength);
            map.put(key, value);
        }
        return map;
    }

    private FieldCodec<?> initDefaultValueFieldCodec(ParsedMapItemMetadata parsedMapItemMetadata) {
        final MapItemMetadata metadata = parsedMapItemMetadata.defaultValueItemMetadata();
        if (XtreamTypes.isBasicType(metadata.getJavaType())) {
            return this.fieldCodecRegistry.getFieldCodec(metadata.getJavaType(), metadata.getLength(), metadata.getCharset(), metadata.isLittleEndian())
                    .orElseThrow(() -> new IllegalArgumentException("Can not determine [FieldCodec] for " + metadata));
        }
        return new DelegateBeanMetadataFieldCodec(this.beanMetadataRegistry.getBeanMetadata(metadata.getJavaType()));
    }

    private Map<Object, FieldCodec<?>> initValueLengthSizeFieldCodec(ParsedMapItemMetadata parsedMapItemMetadata) {
        Map<Object, FieldCodec<?>> map = new HashMap<>();
        parsedMapItemMetadata.valueLengthFieldItemMetadata().forEach((key, value) -> {
            final FieldCodec<?> fieldCodec = this.fieldCodecRegistry.getFieldCodec(value.getJavaType(), value.getLength(), value.getCharset(), value.isLittleEndian())
                    .orElseThrow(() -> new IllegalArgumentException("Can not determine [FieldCodec] for " + value));
            map.put(key, fieldCodec);
        });
        return map;
    }

    private FieldCodec<?> getValueFieldCodec(Object key) {
        return this.specialValueFieldCodec.getOrDefault(key, this.defaultValueFieldCodec);
    }

    private FieldCodec<?> getValueLengthFieldCodec(Object key) {
        return this.valueLengthSizeFieldCodec.getOrDefault(key, this.defaultValueLengthSizeFieldCodec);
    }

    private Map<Object, FieldCodec<?>> initValueFieldCodec(ParsedMapItemMetadata parsedMapItemMetadata) {
        final Map<Object, MapItemMetadata> valueMetadata = parsedMapItemMetadata.valueMetadata();
        final Map<Object, FieldCodec<?>> map = new HashMap<>();
        valueMetadata.forEach((key, value) -> {
            final FieldCodec<?> fieldCodec;
            if (XtreamTypes.isBasicType(value.getJavaType())) {
                fieldCodec = this.fieldCodecRegistry.getFieldCodec(value.getJavaType(), value.getLength(), value.getCharset(), value.isLittleEndian())
                        .orElseThrow(() -> new IllegalArgumentException("Can not determine <FieldCodec> for map value [ whenKeyIs " + key + "]"));
            } else {
                fieldCodec = new DelegateBeanMetadataFieldCodec(this.beanMetadataRegistry.getBeanMetadata(value.getJavaType()));
            }
            map.put(key, fieldCodec);
        });

        return map;
    }

    private Object getMapKey(XtreamFieldMapDescriptor xtreamFieldMapDescriptor, XtreamFieldMapDescriptor.ValueDescriptor valueDescriptor) {
        return switch (xtreamFieldMapDescriptor.keyDescriptor().type()) {
            case i8 -> valueDescriptor.whenKeyIsI8();
            case u8 -> valueDescriptor.whenKeyIsU8();
            case i16 -> valueDescriptor.whenKeyIsI16();
            case u16 -> valueDescriptor.whenKeyIsU16();
            case i32 -> valueDescriptor.whenKeyIsI32();
            case str -> valueDescriptor.whenKeyIsStr();
            default -> throw new UnsupportedOperationException();
        };
    }

    FieldCodec<?> detectKeyFieldCodec(XtreamFieldMapDescriptor xtreamFieldMapDescriptor, ParsedMapItemMetadata parsedMapItemMetadata) {
        final XtreamFieldMapDescriptor.KeyDescriptor keyDescriptor = xtreamFieldMapDescriptor.keyDescriptor();
        final MapItemMetadata keyMetadata = parsedMapItemMetadata.keyMetadata();
        return fieldCodecRegistry.getFieldCodec(keyMetadata.getJavaType(), keyMetadata.getLength(), keyMetadata.getCharset(), keyMetadata.isLittleEndian())
                .orElseThrow(() -> new RuntimeException("Can not determine [FieldCodec] for Key : " + keyDescriptor));
    }

    FieldCodec<?> detectDefaultValueLengthSizeFieldCodec(XtreamFieldMapDescriptor xtreamFieldMapDescriptor, ParsedMapItemMetadata parsedMapItemMetadata) {
        final XtreamFieldMapDescriptor.DefaultValueDescriptor defaultValueDescriptor = xtreamFieldMapDescriptor.defaultValueDescriptor();
        final Class<?> type = switch (defaultValueDescriptor.defaultValueFieldLengthSize()) {
            case 1 -> byte.class;
            case 2 -> short.class;
            case 4 -> int.class;
            default -> throw new RuntimeException("Invalid [defaultValueLengthSize] : " + xtreamFieldMapDescriptor);
        };
        return fieldCodecRegistry.getFieldCodec(type, defaultValueDescriptor.defaultValueFieldLengthSize(), defaultValueDescriptor.defaultCharset(), defaultValueDescriptor.defaultValueFieldLengthSizeIsLittleEndian())
                .orElseThrow(() -> new RuntimeException("Can not determine [FieldCodec] for [defaultValueLengthSize] : " + xtreamFieldMapDescriptor));
    }

    private ParsedMapItemMetadata parseMapItemMetadata(XtreamFieldMapDescriptor mapDescriptor) {
        final XtreamFieldMapDescriptor.DefaultValueDescriptor defaultValueDescriptor = mapDescriptor.defaultValueDescriptor();
        final MapItemMetadata defaultValueMetadata = new MapItemMetadata(defaultValueDescriptor.defaultValueType(), defaultValueDescriptor.defaultValueFieldLengthSize(), defaultValueDescriptor.defaultValueFieldLengthSizeIsLittleEndian(), defaultValueDescriptor.defaultCharset());

        final XtreamFieldMapDescriptor.KeyDescriptor keyDescriptor = mapDescriptor.keyDescriptor();
        final MapItemMetadata keyMetadata = new MapItemMetadata(keyDescriptor.type().getJavaType(), keyDescriptor.type().getSizeInBytes(), keyDescriptor.littleEndian(), keyDescriptor.charset());

        final ParsedMapItemMetadata parsedMapItemMetadata = new ParsedMapItemMetadata(keyMetadata, defaultValueMetadata, new HashMap<>(), new HashMap<>());
        for (final XtreamFieldMapDescriptor.ValueDescriptor valueDescriptor : mapDescriptor.valueDescriptors()) {
            final Object key = getMapKey(mapDescriptor, valueDescriptor);

            // value
            final MapItemMetadata metadata = new MapItemMetadata();
            metadata.setJavaType(valueDescriptor.valueType());
            metadata.setLittleEndian(valueDescriptor.valueIsLittleEndian());
            metadata.setLength(valueDescriptor.valueSize());
            metadata.setCharset(valueDescriptor.valueCharset());
            parsedMapItemMetadata.valueMetadata().put(key, metadata);

            // valueLengthField override
            final MapItemMetadata valueLengthFieldMetadata = new MapItemMetadata();
            int valueLengthFieldSize = valueDescriptor.valueLengthFieldSize() > 0 ? valueDescriptor.valueLengthFieldSize() : mapDescriptor.defaultValueDescriptor().defaultValueFieldLengthSize();
            switch (valueLengthFieldSize) {
                case 1 -> valueLengthFieldMetadata.setJavaType(Short.class);
                case 2 -> valueLengthFieldMetadata.setJavaType(Integer.class);
                case 4 -> valueLengthFieldMetadata.setJavaType(Long.class);
                default -> throw new IllegalArgumentException();
            }
            valueLengthFieldMetadata.setCharset(null);
            valueLengthFieldMetadata.setLittleEndian(valueDescriptor.valueLengthFieldSizeIsLittleEndian());
            valueLengthFieldMetadata.setLength(valueLengthFieldSize);
            parsedMapItemMetadata.valueLengthFieldItemMetadata().put(key, valueLengthFieldMetadata);
        }
        return parsedMapItemMetadata;
    }


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MapItemMetadata {
        Class<?> javaType;
        private int length;
        private boolean littleEndian;
        private String charset;
    }


    public record ParsedMapItemMetadata(
            MapItemMetadata keyMetadata,
            MapItemMetadata defaultValueItemMetadata,
            Map<Object, MapItemMetadata> valueLengthFieldItemMetadata,
            Map<Object, MapItemMetadata> valueMetadata) {
    }
}
