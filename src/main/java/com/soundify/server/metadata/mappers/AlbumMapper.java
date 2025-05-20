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
import org.mapstruct.Named;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
@DecoratedWith(AlbumMapperDecorator.class)
public interface AlbumMapper {

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "albumLocaleToLocalTag")
    @Mapping(source = "notAvailableLocales", target = "notAvailableLocaleTags", qualifiedByName = "notAvailableToNotAvailableTag")
    AlbumResponse toAlbumResponse(Album album);

    @Mapping(source = "locale", target = "localeTag", qualifiedByName = "albumLocaleToLocalTag")
    @Mapping(source = "notAvailableLocales", target = "notAvailableLocaleTags", qualifiedByName = "notAvailableToNotAvailableTag")
    List<AlbumResponse> toAlbumResponses(List<Album> albums);

    Album createAlbumFromRequest(AlbumRequest request);

    Album updateAlbumFromRequest(Id id, AlbumUpdateRequest request);

    @Named("albumLocaleToLocalTag")
    default String albumLocaleToLocalTag(Locale locale) {
        return locale != null ? locale.toLanguageTag() : null;
    }

    @Named("albumLocaleTagToLocale")
    default Locale albumLocaleTagToLocale(String localeTag) {
        return localeTag == null ? null : Locale.forLanguageTag(localeTag);
    }

    @Named("notAvailableToNotAvailableTag")
    default Set<String> notAvailableToNotAvailableTag(Set<Locale> notAvailableLocales) {
        return notAvailableLocales.stream()
                .map(Locale::toLanguageTag)
                .collect(Collectors.toSet());
    }
}
