package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.BadRequestException;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrackServiceImpl implements TrackService {
    TrackRepository trackRepository;
    TrackMapper trackMapper;

    @Override
    public TrackResponse getById(Id id) {
        // Throw temp exception
        return trackMapper.toTrackResponse(trackRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found")));
    }

    @Override
    public List<TrackResponse> getByIds(@NotNull List<Id> ids) {
        return trackMapper.toTrackResponses(trackRepository.findAllById(ids));
    }

    @Transactional
    @Override
    public Id create(TrackRequest trackRequest) {
        // Để tạm thời, sẽ thay đổi lại mã lỗi
        throw new BadRequestException("Method not implemented");
    }

    @Transactional
    @Override
    public void update(Id id, TrackRequest trackRequest) {
        Track track = trackMapper.updateTrackFromRequest(id, trackRequest);
        trackRepository.saveAndFlush(track);
    }

    @Transactional
    @Override
    public void delete(Id id) {
        trackRepository.deleteById(id);
    }
}
