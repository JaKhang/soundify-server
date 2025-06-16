package com.soundify.server.search.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record SearchRequest(
        @NotBlank String query,
        @NotNull @Min(0) Integer page,
        @NotNull @Min(1) Integer size,
        @NotBlank String sortBy,
        @NotBlank String sortDir
) {
    public SearchRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size < 1) size = 10;
        if (sortBy == null || sortBy.isBlank()) sortBy = "created_at";
        if (sortDir == null || sortDir.isBlank()) sortDir = "DESC";
    }

    public Pageable toPageable() {
        Sort.Direction direction = getSortDirection();
        Sort sort = Sort.by(direction, this.sortBy);
        return PageRequest.of(this.page, this.size, sort);
    }

    public Sort.Direction getSortDirection() {
        if (sortDir == null || sortDir.isBlank()) {
            return Sort.Direction.DESC; // mặc định
        }

        try {
            return Sort.Direction.fromString(sortDir);
        } catch (IllegalArgumentException e) {
            return Sort.Direction.DESC;
        }
    }

    public String getCleanQuery() {
        return this.query.trim().toLowerCase();
    }
}
