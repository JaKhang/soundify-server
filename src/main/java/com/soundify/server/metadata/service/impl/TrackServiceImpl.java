package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.converter.impl.TrackConverter;
import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.NotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrackServiceImpl implements TrackService {
    TrackRepository trackRepository;
    AlbumRepository albumRepository;
    ArtistRepository artistRepository;
    TrackMapper trackMapper;
    TrackConverter trackConverter;

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
    public String create(TrackRequest trackRequest) {
        Track track = trackConverter.toEntity(trackRequest);
        Id savedCid = trackRepository.save(track).getId();
        return savedCid.toString();
    }

    @Override
    public void update(TrackRequest trackRequest) {
        Track track = trackConverter.toEntity(trackRequest);
        trackRepository.save(track);
    }

    @Override
    public void delete(String id) {
        trackRepository.deleteById(Id.from(id));
    }
}
