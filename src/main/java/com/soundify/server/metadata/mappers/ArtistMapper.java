package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.mappers.decorators.ArtistMapperDecorator;
import com.soundify.server.shared.domain.Id;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Locale;

@Mapper(componentModel = "spring")
@DecoratedWith(ArtistMapperDecorator.class)
public interface ArtistMapper {
    // Use qualifiedByName to map locale -> localeTag. Format: BCP 47 Tag
    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "artistLocaleToLocalTag")
    ArtistResponse toArtistResponse(Artist artist);

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "artistLocaleToLocalTag")
    List<ArtistResponse> toArtistResponses(List<Artist> artists);

    @Mapping(target = "id", expression = "java(generateId())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "localeTag", target = "locale", qualifiedByName = "artistLocaleTagToLocale")
    Artist createArtistFromRequest(ArtistRequest artistRequest);

    Artist updateArtistFromRequest(Id id, ArtistRequest artistRequest);

    @Named("artistLocaleToLocalTag")
    default String artistLocaleToLocalTag(Locale locale) {
        return locale != null ? locale.toLanguageTag() : Locale.ROOT.toLanguageTag();
    }

    @Named("artistLocaleTagToLocale")
    default Locale artistLocaleTagToLocale(String localeTag) {
        return localeTag == null ? Locale.ROOT : Locale.forLanguageTag(localeTag);
    }

    default Id generateId() {
        return Id.fast();
    }
}
