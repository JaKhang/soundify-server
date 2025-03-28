package com.soundify.server.metadata.converter;

import java.util.List;

public interface Converter<E, D> {
    E toEntity(D request);
    List<E> toEntities(List<D> request);
}
