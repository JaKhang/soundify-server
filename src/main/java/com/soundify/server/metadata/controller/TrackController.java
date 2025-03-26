package com.soundify.server.metadata.controller;

import com.soundify.server.metadata.dto.track.TrackResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController("/api/v1/tracks")
public class TrackController {
    @GetMapping(value = "/{id}")
    @ResponseBody
    public TrackResponse getTrackById(@PathVariable String id) {
        return null;
    }
}
