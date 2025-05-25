package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.shared.domain.Id;

import java.util.List;

public interface TrackService {
    TrackResponse getById(Id id);

    List<TrackResponse> getByIds(List<Id> ids);

    Id create(TrackRequest trackRequest);

    void update(Id id, TrackRequest trackRequest);

    void delete(Id id);
}
