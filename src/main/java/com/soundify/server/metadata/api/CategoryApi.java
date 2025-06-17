package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.category.CategoryRequest;
import com.soundify.server.metadata.dto.category.CategoryResponse;
import com.soundify.server.metadata.service.AlbumService;
import com.soundify.server.metadata.service.CategoryService;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@RequestMapping("v1/catalog/categories")
public class CategoryApi {
    CategoryService categoryService;
    AlbumService albumService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Id id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getByIds(@RequestParam List<Id> ids) {
        return ResponseEntity.ok(categoryService.getByIds(ids));
    }

    // Hàm sort album theo popularity DESC, định nghĩa trong service
    @GetMapping("/{id}/albums")
    public ResponseEntity<List<AlbumResponse>> getAlbumsByCategoryId(@PathVariable Id id,
                                                                     @RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                                     @RequestParam(defaultValue = "5") @Min(1) Integer size) {
        return ResponseEntity.ok(albumService.getByCategoryId(id, page, size));
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryRequest categoryRequest, UriComponentsBuilder uriBuilder) {
        Id id = categoryService.create(categoryRequest);
        URI uri = uriBuilder
                .path("/api/v1/categories/{id}")
                .buildAndExpand(id.toString())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
