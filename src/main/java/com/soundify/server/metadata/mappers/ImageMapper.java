package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.image.ImageResponse;
import com.soundify.server.shared.data.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponse toImageResponse(Image image);
}
