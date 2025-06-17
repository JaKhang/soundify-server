package com.soundify.server.search.api;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.search.dto.SearchRequest;
import com.soundify.server.search.service.SearchService;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController

@RequestMapping("/v1/search")
public class SearchApi {
    SearchService searchService;

    @GetMapping("/tracks")
    public ResponseEntity<List<TrackResponse>> searchTracks(@RequestParam String query,
                                                            @RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                            @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                                            @RequestParam(defaultValue = "DESC") String sortDir) {
        SearchRequest searchRequest = new SearchRequest(query, page, size, sortBy, sortDir);
        List<TrackResponse> tracks = searchService.searchTracks(searchRequest);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumResponse>> searchAlbums(@RequestParam String query,
                                                            @RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                            @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                                            @RequestParam(defaultValue = "DESC") String sortDir) {
        SearchRequest searchRequest = new SearchRequest(query, page, size, sortBy, sortDir);
        List<AlbumResponse> albums = searchService.searchAlbums(searchRequest);
        return ResponseEntity.ok(albums);
    }

    @GetMapping("/artists")
    public ResponseEntity<List<ArtistResponse>> searchArtists(@RequestParam String query,
                                                              @RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                              @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(defaultValue = "DESC") String sortDir) {
        SearchRequest searchRequest = new SearchRequest(query, page, size, sortBy, sortDir);
        List<ArtistResponse> artists = searchService.searchArtists(searchRequest);
        return ResponseEntity.ok(artists);
    }
}
