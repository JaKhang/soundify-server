package com.soundify.server.search.service.impl;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.search.dto.SearchRequest;
import com.soundify.server.search.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    TrackRepository trackRepository;
    TrackMapper trackMapper;

    @Override
    public List<TrackResponse> searchTracks(SearchRequest searchRequest) {
        String query = searchRequest.getCleanQuery();
        Pageable pageable = searchRequest.toPageable();
        Page<Track> tracks = trackRepository.searchTracks(query, pageable);
        return trackMapper.toTrackResponses(tracks.getContent());
    }
}
