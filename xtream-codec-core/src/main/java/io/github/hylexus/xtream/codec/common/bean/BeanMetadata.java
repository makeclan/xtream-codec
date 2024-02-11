package io.github.hylexus.xtream.codec.common.bean;

import io.github.hylexus.xtream.codec.core.utils.BeanUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Constructor;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BeanMetadata {
    private final Class<?> rawType;
    private final Constructor<?> constructor;
    private final List<BeanPropertyMetadata> propertyMetadataList;

    public BeanMetadata(Class<?> rawType, Constructor<?> constructor, List<BeanPropertyMetadata> propertyMetadataList) {
        this.rawType = rawType;
        this.constructor = constructor;
        this.propertyMetadataList = propertyMetadataList;
    }

    public Object createNewInstance() {
        return BeanUtils.createNewInstance(this.getConstructor(), (Object[]) null);
    }
}
