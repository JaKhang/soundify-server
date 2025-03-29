package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.mappers.AlbumMapper;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.service.AlbumService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumServiceImpl implements AlbumService {
    AlbumRepository albumRepository;
    AlbumMapper albumMapper;

    @Override
    public AlbumResponse getById(Id id) {
        return albumMapper.toAlbumResponse(albumRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found")));
    }

    @Override
    public List<AlbumResponse> getByIds(List<Id> cids) {
        return albumMapper.toAlbumResponses(albumRepository
                .findAllById(cids));
    }
}
