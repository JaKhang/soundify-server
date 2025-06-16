package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.category.CategoryRequest;
import com.soundify.server.metadata.dto.category.CategoryResponse;
import com.soundify.server.metadata.entities.Category;
import com.soundify.server.metadata.mappers.CategoryMapper;
import com.soundify.server.metadata.repositories.CategoryRepository;
import com.soundify.server.metadata.service.CategoryService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse getById(Id id) {
        return categoryMapper.toCategoryResponse(categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    @Override
    public List<CategoryResponse> getByIds(List<Id> cids) {
        List<Category> category = categoryRepository.findAllById(cids);
        return categoryMapper.toCategoryResponses(category);
    }

    @Override
    public Id create(CategoryRequest albumRequest) {
        return categoryRepository.save(categoryMapper.createCategory(albumRequest)).getId();
    }

    @Override
    public void update(Id id, CategoryRequest albumUpdateRequest) {

    }

    @Override
    public void delete(Id id) {

    }
}
