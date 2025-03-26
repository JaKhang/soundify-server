package com.soundify.server.metadata.controller;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {
    @GetMapping(value = "/{id}")
    public AlbumResponse getAlbumById(@PathVariable String id) {
        return null;
    }
}
