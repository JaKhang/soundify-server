package com.soundify.server.search.api;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.search.dto.SearchRequest;
import com.soundify.server.search.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@PostAuthorize("isAnonymous()")

@RequestMapping("/v1/search")
public class SearchApi {
    SearchService searchService;

    @GetMapping("/tracks")
    public ResponseEntity<List<TrackResponse>> searchTracks(@RequestParam SearchRequest searchRequest) {
        List<TrackResponse> tracks = searchService.searchTracks(searchRequest);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/albums")
    public String searchAlbums() {
        return "searchAlbums";
    }

    @GetMapping("/artists")
    public String searchArtists() {
        return "searchArtists";
    }
}
