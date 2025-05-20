package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.category.CategoryRequest;
import com.soundify.server.metadata.dto.category.CategoryResponse;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CategoryService {
    CategoryResponse getById(@NotNull Id id);

    List<CategoryResponse> getByIds(@NotEmpty List<Id> cids);

    Id create(@Valid CategoryRequest albumRequest);

    void update(@NotNull Id id, @Valid CategoryRequest albumUpdateRequest);

    void delete(@NotNull Id id);
}
