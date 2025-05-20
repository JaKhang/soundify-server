package com.soundify.server.metadata.dto.category;

import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CategoryRequest(@NotBlank String name, @NotBlank String localeTag,
                              @NotEmpty Set<Id> albumIds, @NotEmpty Set<Image> icons, @Min(0) int order) {
}
