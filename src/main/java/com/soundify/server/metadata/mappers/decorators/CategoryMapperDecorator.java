package com.soundify.server.metadata.mappers.decorators;

import com.soundify.server.metadata.dto.category.CategoryRequest;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Category;
import com.soundify.server.metadata.mappers.CategoryMapper;
import com.soundify.server.metadata.mappers.LocaleConverter;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.CategoryRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class CategoryMapperDecorator implements CategoryMapper {
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    LocaleConverter localeConverter;

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        List<Album> listAlbum = albumRepository.findAllById(categoryRequest.albumIds());

        return Category.builder()
                .name(categoryRequest.name())
                .locale(localeConverter.localeTagToLocale(categoryRequest.localeTag()))
                .icons(categoryRequest.icons())
                .orderBy(categoryRequest.order())
                .albums(new HashSet<>(listAlbum))
                .build();
    }

    @Override
    public Category updateCategory(Id id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Album> listAlbum = albumRepository.findAllById(categoryRequest.albumIds());
        existingCategory.getAlbums().clear();
        existingCategory.getAlbums().addAll(listAlbum);

        return Category.builder()
                .id(existingCategory.getId())
                .name(categoryRequest.name())
                .locale(localeConverter.localeTagToLocale(categoryRequest.localeTag()))
                .icons(categoryRequest.icons())
                .orderBy(categoryRequest.order())
                .albums(new HashSet<>(listAlbum))
                .deleted(existingCategory.isDeleted())
                .createdAt(existingCategory.getCreatedAt())
                .build();
    }
}
