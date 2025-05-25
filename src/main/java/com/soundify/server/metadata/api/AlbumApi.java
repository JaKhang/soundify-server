package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.service.AlbumService;
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
@RequestMapping("/api/v1/albums")
public class AlbumApi {
    AlbumService albumService;

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getAlbumById(@PathVariable Id id) {
        return ResponseEntity.ok(albumService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getAlbumByIds(@RequestParam List<Id> ids) {
        return ResponseEntity.ok(albumService.getByIds(ids));
    }

    @PostMapping
    public ResponseEntity<Void> createAlbum(@RequestBody @Valid AlbumResponse albumResponse, UriComponentsBuilder uriBuilder) {
        Id id = albumService.create(albumResponse);
        URI uri = uriBuilder
                .path("/api/v1/albums/{id}")
                .buildAndExpand(id.toString())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlbum(@PathVariable Id id, @RequestBody @Valid AlbumResponse albumResponse) {
        albumService.update(id, albumResponse);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Id id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
