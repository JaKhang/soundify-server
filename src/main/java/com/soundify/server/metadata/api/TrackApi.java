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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tracks")
public class TrackApi {
    TrackService trackService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse<TrackResponse>> getTrackById(@PathVariable String id) {
        Id cid = Id.from(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", trackService.getById(cid)));
    }

    @GetMapping("/list/{ids}")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getTrackByIds(@PathVariable String ids) {
        String[] decodeIds = URLDecoder.decode(ids, StandardCharsets.UTF_8).split(",");
        List<Id> cids = Arrays.stream(decodeIds)
                .map(Id::from)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", trackService.getByIds(cids)));
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

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> updateTrack(@PathVariable String id, @RequestBody @Valid TrackRequest trackRequest) {
        trackService.update(id, trackRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "Updated Success", null));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteTrack(@PathVariable String id) {
        trackService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Deleted Success", null));
    }
}
