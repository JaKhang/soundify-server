package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<TrackResponse>> getTrackById(@PathVariable Id id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", trackService.getById(id)));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getTrackByIds(@RequestParam List<Id> ids) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", trackService.getByIds(ids)));
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> createTrack(@RequestBody @Valid TrackRequest trackRequest, UriComponentsBuilder uriBuilder) {
        String id = trackService.create(trackRequest);
        // Create URI and attach to header
        URI uri = uriBuilder
                .path("/api/v1/tracks/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uri).body(new ApiResponse<>(201, "Created Success", null));
    }

    @PutMapping("/")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> updateTrack(@RequestBody @Valid TrackRequest trackRequest) {
        trackService.update(trackRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "Updated Success", null));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteTrack(@PathVariable String id) {
        trackService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Deleted Success", null));
    }
}
