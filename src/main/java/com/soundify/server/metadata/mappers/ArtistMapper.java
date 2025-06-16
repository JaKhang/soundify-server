package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.mappers.decorators.ArtistMapperDecorator;
import com.soundify.server.shared.domain.Id;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocaleConverter.class})
@DecoratedWith(ArtistMapperDecorator.class)
public interface ArtistMapper {
    // Use qualifiedByName to map locale -> localeTag. Format: BCP 47 Tag
    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "localeToLocalTag")
    ArtistResponse toArtistResponse(Artist artist);

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "localeToLocalTag")
    List<ArtistResponse> toArtistResponses(List<Artist> artists);

    @Mapping(target = "id", expression = "java(generateId())")
    @Mapping(source = "localeTag", target = "locale", qualifiedByName = "localeTagToLocale")
    Artist createArtistFromRequest(ArtistRequest artistRequest);

    Artist updateArtistFromRequest(Id id, ArtistRequest artistRequest);

    default Id generateId() {
        return Id.fast();
    }
}
