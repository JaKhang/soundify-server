package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.exceptions.NotFoundException;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrackServiceImpl implements TrackService {
    TrackRepository trackRepository;
    AlbumRepository albumRepository;
    ArtistRepository artistRepository;
    TrackMapper trackMapper;

    @Override
    public TrackResponse getById(Id id) {
        // Throw temp exception
        return trackMapper.toTrackResponse(trackRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Track not found")));
    }

    @Override
    public List<TrackResponse> getByIds(@NotNull List<Id> ids) {
        return trackMapper.toTrackResponses(trackRepository.findAllById(ids));
    }

    @Override
    public void create(TrackRequest trackRequest) {
        trackRepository.save(trackMapper.toTrack(trackRequest, albumRepository, artistRepository));
    }
}
