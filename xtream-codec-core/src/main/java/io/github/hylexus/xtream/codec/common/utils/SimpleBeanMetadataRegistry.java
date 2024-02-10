package io.github.hylexus.xtream.codec.common.utils;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.BasicBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.NestedBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.common.bean.impl.SequenceBeanPropertyMetadata;
import io.github.hylexus.xtream.codec.core.BeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.FieldCodec;
import io.github.hylexus.xtream.codec.core.FieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.annotation.XtreamField;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.function.Function;

// todo 重构
public class SimpleBeanMetadataRegistry implements BeanMetadataRegistry {
    private final Map<Class<?>, BeanMetadata> cache = new HashMap<>();
    private final FieldCodecRegistry fieldCodecRegistry;

    public SimpleBeanMetadataRegistry() {
        fieldCodecRegistry = new FieldCodecRegistry();
    }

    private static int getLength(XtreamField xtreamField, Class<?> rawClassType) {
        if (xtreamField.length() > 0) {
            return xtreamField.length();
        }

        return XtreamTypes.getDefaultSizeInBytes(rawClassType).orElseGet(xtreamField::length);
    }

    @Override
    public BeanMetadata getBeanMetadata(Class<?> beanClass) {
        return this.getBeanMetadata(beanClass, this::createBeanPropertyMetadata);
    }

    @Override
    public BeanMetadata getBeanMetadata(Class<?> beanClass, Function<PropertyDescriptor, BeanPropertyMetadata> creator) {
        BeanMetadata result = cache.get(beanClass);
        if (result != null) {
            return result;
        }

        synchronized (SimpleBeanMetadataRegistry.class) {
            if ((result = cache.get(beanClass)) != null) {
                return result;
            }
            result = doGetMetadata(beanClass, creator);
            cache.put(beanClass, result);
            return result;
        }

    }

    public BasicBeanPropertyMetadata createBeanPropertyMetadata(PropertyDescriptor pd) {
        final BeanUtils.BasicPropertyDescriptor basicPropertyDescriptor = (BeanUtils.BasicPropertyDescriptor) pd;
        final BasicBeanPropertyMetadata metadata = new BasicBeanPropertyMetadata(
                pd.getName(),
                pd.getPropertyType(),
                basicPropertyDescriptor.getField(),
                // pd,
                BeanUtils.createGetter(basicPropertyDescriptor),
                BeanUtils.createSetter(basicPropertyDescriptor)
        );
        metadata.setFieldCodec(detectFieldCodec(metadata));
        return metadata;
    }

    public BeanMetadata doGetMetadata(Class<?> beanClass, Function<PropertyDescriptor, BeanPropertyMetadata> creator) {
        final BeanInfo beanInfo = BeanUtils.getBeanInfo(beanClass, field -> AnnotatedElementUtils.findMergedAnnotation(field, XtreamField.class) != null);
        final BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        final ArrayList<BeanPropertyMetadata> pdList = new ArrayList<>();
        for (final PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            final BeanPropertyMetadata basicPropertyMetadata = creator.apply(pd);
            if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.basic) {
                pdList.add(basicPropertyMetadata);
            } else if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.nested) {
                final BeanMetadata nestedMetadata = doGetMetadata(pd.getPropertyType(), creator);
                final NestedBeanPropertyMetadata metadata = new NestedBeanPropertyMetadata(nestedMetadata, basicPropertyMetadata);
                pdList.add(metadata);
            } else if (basicPropertyMetadata.dataType() == BeanPropertyMetadata.FiledDataType.sequence) {
                final List<Class<?>> genericClass = getGenericClass(basicPropertyMetadata.field());
                final BeanMetadata valueMetadata = doGetMetadata(genericClass.getFirst(), creator);
                final NestedBeanPropertyMetadata metadata = new NestedBeanPropertyMetadata(valueMetadata, basicPropertyMetadata);
                final SequenceBeanPropertyMetadata seqMetadata = new SequenceBeanPropertyMetadata(basicPropertyMetadata, metadata);
                pdList.add(seqMetadata);
            } else {
                throw new IllegalStateException("Cannot determine dataType for " + basicPropertyMetadata.field());
            }
        }
        pdList.sort(Comparator.comparing(BeanPropertyMetadata::order));
        return new BeanMetadata(beanInfo.getBeanDescriptor().getBeanClass(), BeanUtils.getConstructor(beanDescriptor), pdList);
    }

    private List<Class<?>> getGenericClass(Field field) {
        final Type genericType = field.getGenericType();
        final List<Class<?>> list = new ArrayList<>();
        if (genericType instanceof ParameterizedType) {
            for (Type actualTypeArgument : ((ParameterizedType) genericType).getActualTypeArguments()) {
                // ignore WildcardType
                // ?, ? extends Number, or ? super Integer
                if (actualTypeArgument instanceof WildcardType) {
                    continue;
                }
                list.add((Class<?>) actualTypeArgument);
            }
        }
        return list;
    }

    public FieldCodec<?> detectFieldCodec(BasicBeanPropertyMetadata metadata) {

        final XtreamField xtreamField = metadata.findAnnotation(XtreamField.class)
                .orElseThrow();

        if (xtreamField.fieldCodec() != FieldCodec.Placeholder.class) {
            @SuppressWarnings({"rawtypes"}) final Class<? extends FieldCodec> aClass = xtreamField.fieldCodec();
            // return createNewInstance(aClass, (Object) null);
            return BeanUtils.createNewInstance(aClass);
        }

        final Class<?> rawClassType = metadata.rawClass();
        final int length = getLength(xtreamField, rawClassType);

        final Optional<FieldCodec<?>> fieldCodec = fieldCodecRegistry.getFieldCodec(rawClassType, length, xtreamField.charset(), xtreamField.littleEndian());
        if (fieldCodec.isPresent()) {
            return fieldCodec.get();
        }

        return switch (metadata.dataType()) {
            case nested, sequence -> null;
            default -> throw new IllegalStateException("Cannot determine FieldCodec for " + rawClassType);
        };
    }
}
