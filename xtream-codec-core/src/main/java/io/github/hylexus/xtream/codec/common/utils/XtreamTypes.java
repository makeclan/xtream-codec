package io.github.hylexus.xtream.codec.common.utils;

import java.util.*;

public class XtreamTypes {
    final static Map<Class<?>, Integer> DEFAULT_SIZE_MAPPING = new HashMap<>();
    static final Set<Class<?>> BASIC_TYPES = Collections.unmodifiableSet(DEFAULT_SIZE_MAPPING.keySet());

    static {
        DEFAULT_SIZE_MAPPING.put(byte.class, 1);
        DEFAULT_SIZE_MAPPING.put(Byte.class, 1);
        DEFAULT_SIZE_MAPPING.put(char.class, 1);
        DEFAULT_SIZE_MAPPING.put(Character.class, 1);
        DEFAULT_SIZE_MAPPING.put(short.class, 2);
        DEFAULT_SIZE_MAPPING.put(Short.class, 2);
        DEFAULT_SIZE_MAPPING.put(int.class, 4);
        DEFAULT_SIZE_MAPPING.put(Integer.class, 4);
        DEFAULT_SIZE_MAPPING.put(float.class, 4);
        DEFAULT_SIZE_MAPPING.put(Float.class, 4);
        DEFAULT_SIZE_MAPPING.put(long.class, 8);
        DEFAULT_SIZE_MAPPING.put(Long.class, 8);
        DEFAULT_SIZE_MAPPING.put(double.class, 8);
        DEFAULT_SIZE_MAPPING.put(Double.class, 8);
    }

    public static boolean isNumberType(Class<?> targetType) {
        return BASIC_TYPES.contains(targetType);
    }

    public static boolean isBasicType(Class<?> targetType) {
        return isNumberType(targetType)
                || String.class == targetType
                || byte[].class == targetType
                || Byte[].class == targetType;
    }

    public static Optional<Integer> getDefaultSizeInBytes(Class<?> cls) {
        return Optional.ofNullable(DEFAULT_SIZE_MAPPING.get(cls));
    }
}
