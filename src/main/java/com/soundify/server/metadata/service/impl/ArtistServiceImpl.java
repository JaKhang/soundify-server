package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.mappers.ArtistMapper;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.service.ArtistService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ArtistServiceImpl implements ArtistService {
    ArtistRepository artistRepository;
    ArtistMapper artistMapper;

    @Override
    public ArtistResponse getById(Id id) {
        return artistMapper.toArtistResponse(artistRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Artist not found")));
    }

    @Override
    public List<ArtistResponse> getByIds(List<Id> ids) {
        return artistMapper.toArtistResponses(artistRepository.findAllById(ids));
    }

    @Override
    public Id create(ArtistRequest artistRequest) {
        Artist artist = artistMapper.createArtistFromRequest(artistRequest);
        return artistRepository.save(artist).getId();
    }

    @Override
    public void update(Id id, ArtistRequest artistRequest) {

    }

    @Override
    public void delete(Id id) {

    }
}
