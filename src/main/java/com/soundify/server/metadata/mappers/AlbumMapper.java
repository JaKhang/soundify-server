package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.album.AlbumRequest;
import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.album.AlbumUpdateRequest;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.mappers.decorators.AlbumMapperDecorator;
import com.soundify.server.shared.domain.Id;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class, LocaleConverter.class})
@DecoratedWith(AlbumMapperDecorator.class)
public interface AlbumMapper {

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "localeToLocalTag")
    @Mapping(source = "notAvailableLocales", target = "notAvailableLocaleTags", qualifiedByName = "notAvailableToNotAvailableTag")
    AlbumResponse toAlbumResponse(Album album);

    List<AlbumResponse> toAlbumResponses(List<Album> albums);

    Album createAlbumFromRequest(AlbumRequest request);
    Album updateAlbumFromRequest(Id id, AlbumUpdateRequest request);
}
