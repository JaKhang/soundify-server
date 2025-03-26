package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
                        .orElseThrow(() -> new NotFoundException("Track not found", ErrorCode.NOT_FOUND)));
    }
}
