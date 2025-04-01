package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tracks")
public class TrackApi {
    TrackService trackService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TrackResponse> getTrackById(@PathVariable Id id) {
        return ResponseEntity.ok(trackService.getById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TrackResponse>> getTrackByIds(@RequestParam List<Id> ids) {
        return ResponseEntity.ok(trackService.getByIds(ids));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> createTrack(@RequestBody @Valid TrackRequest trackRequest, UriComponentsBuilder uriBuilder) {
        // TODO: Change to 501 NOT IMPLEMENTED (create exception in base branch)
        Id id = trackService.create(trackRequest);
        // Create URI and attach to header
        URI uri = uriBuilder
                .path("/api/v1/tracks/{id}")
                .buildAndExpand(id.toString())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateTrack(@PathVariable Id id, @RequestBody @Valid TrackRequest trackRequest) {
        trackService.update(id, trackRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTrack(@PathVariable Id id) {
        trackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
