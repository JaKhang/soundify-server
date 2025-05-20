package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.service.ArtistService;
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
@RequestMapping("api/v1/artists")
public class ArtistApi {
    ArtistService artistService;

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> getArtistById(@PathVariable Id id) {
        return ResponseEntity.ok(artistService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponse>> getArtistsByIds(@RequestParam List<Id> ids) {
        return ResponseEntity.ok(artistService.getByIds(ids));
    }

    @PostMapping
    public ResponseEntity<Void> createArtist(@RequestBody @Valid ArtistRequest artistRequest, UriComponentsBuilder uriBuilder) {
        Id id = artistService.create(artistRequest);
        URI uri = uriBuilder
                .path("/api/v1/artists/{id}")
                .buildAndExpand(id.toString())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArtist(@PathVariable Id id, @RequestBody @Valid ArtistRequest artistRequest) {
        artistService.update(id, artistRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Id id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
