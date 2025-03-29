package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.entities.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
public interface AlbumMapper {

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "albumLocaleToLocalTag")
    @Mapping(source = "notAvailableLocales", target = "notAvailableLocaleTags", qualifiedByName = "notAvailableToNotAvailableTag")
    AlbumResponse toAlbumResponse(Album album);

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "albumLocaleToLocalTag")
    @Mapping(source = "notAvailableLocales", target = "notAvailableLocaleTags", qualifiedByName = "notAvailableToNotAvailableTag")
    List<AlbumResponse> toAlbumResponses(List<Album> albums);

    @Named("albumLocaleToLocalTag")
    default String albumLocaleToLocalTag(Locale locale) {
        return locale != null ? locale.toLanguageTag() : null;
    }

    @Named("notAvailableToNotAvailableTag")
    default Set<String> notAvailableToNotAvailableTag(Set<Locale> notAvailableLocales) {
        return notAvailableLocales.stream()
                .map(Locale::toLanguageTag)
                .collect(Collectors.toSet());
    }
}
