package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.category.CategoryRequest;
import com.soundify.server.metadata.dto.category.CategoryResponse;
import com.soundify.server.metadata.service.CategoryService;
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
@RequestMapping("api/v1/categories")
public class CategoryApi {
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Id id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getByIds(@RequestParam List<Id> ids) {
        return ResponseEntity.ok(categoryService.getByIds(ids));
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
