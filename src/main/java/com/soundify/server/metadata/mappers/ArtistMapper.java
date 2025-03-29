package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.entities.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    // Use qualifiedByName to map locale -> localeTag. Format: BCP 47 Tag
    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "artistLocaleToLocalTag")
    ArtistResponse toArtistResponse(Artist artist);

    @Named("artistLocaleToLocalTag")
    default String artistLocaleToLocalTag(Locale locale) {
        return locale != null ? locale.toLanguageTag() : Locale.ROOT.toLanguageTag();
    }
}
