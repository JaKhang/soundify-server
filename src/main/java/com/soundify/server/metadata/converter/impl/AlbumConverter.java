package com.soundify.server.metadata.converter.impl;

import com.soundify.server.metadata.converter.Converter;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AlbumConverter implements Converter<Album, Id> {
    AlbumRepository albumRepository;

    @Override
    public Album toEntity(Id id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));
    }

    @Override
    public List<Album> toEntities(List<Id> ids) {
        return albumRepository.findAllById(ids);
    }
}
