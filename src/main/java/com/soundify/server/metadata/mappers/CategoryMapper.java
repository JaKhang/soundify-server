package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.category.CategoryRequest;
import com.soundify.server.metadata.dto.category.CategoryResponse;
import com.soundify.server.metadata.entities.Category;
import com.soundify.server.metadata.mappers.decorators.CategoryMapperDecorator;
import com.soundify.server.shared.domain.Id;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class, LocaleConverter.class})
@DecoratedWith(CategoryMapperDecorator.class)
public interface CategoryMapper {

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = {"localeToLocalTag"})
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "localeToLocalTag")
    List<CategoryResponse> toCategoryResponses(List<Category> categories);

    Category createCategory(CategoryRequest categoryRequest);

    Category updateCategory(Id id, CategoryRequest categoryRequest);
}
