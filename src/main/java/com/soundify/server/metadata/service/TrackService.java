package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.shared.domain.Id;

public interface TrackService {
    TrackResponse getById(Id id);
}
