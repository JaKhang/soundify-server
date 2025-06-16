package com.soundify.server.search.service;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.search.dto.SearchRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface SearchService {
    List<TrackResponse> searchTracks(@NotNull SearchRequest searchRequest);
}
