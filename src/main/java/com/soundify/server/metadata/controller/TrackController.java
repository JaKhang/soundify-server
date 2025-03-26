package com.soundify.server.metadata.controller;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tracks")
public class TrackController {
    TrackService trackService;

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TrackResponse> getTrackById(@PathVariable Id id) {
        return ResponseEntity.ok(trackService.getById(id));
    }
}
