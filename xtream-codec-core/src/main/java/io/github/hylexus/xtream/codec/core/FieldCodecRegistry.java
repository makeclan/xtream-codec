package io.github.hylexus.xtream.codec.core;

import io.github.hylexus.xtream.codec.common.bean.BeanPropertyMetadata;

import java.util.Optional;

public interface FieldCodecRegistry {

    Optional<FieldCodec<?>> getFieldCodec(BeanPropertyMetadata propertyMetadata);

    Optional<FieldCodec<?>> getFieldCodec(Class<?> targetType, int sizeInBytes, String charset, boolean littleEndian);

}
