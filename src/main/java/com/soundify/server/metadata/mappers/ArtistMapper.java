package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.SimplifiedArtist;
import com.soundify.server.metadata.entities.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    // Use qualifiedByName to map locale -> localeTag. Format: BCP 47 Tag
    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "localeToLocalTag")
    SimplifiedArtist toSimplifiedArtist(Artist artist);

    @Named("localeToLocalTag")
    default String localeToLocalTag(Locale locale) {
        return locale != null ? locale.toLanguageTag() : null;
    }
}
