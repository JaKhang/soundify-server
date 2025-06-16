package com.soundify.server.metadata.mappers.decorators;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.mappers.ArtistMapper;
import com.soundify.server.metadata.mappers.LocaleConverter;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ArtistMapperDecorator implements ArtistMapper {
    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    LocaleConverter localeConverter;

    @Override
    public Artist updateArtistFromRequest(Id id, ArtistRequest artistRequest) {
        Artist searchArtist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        return Artist.builder()
                .id(searchArtist.getId())
                .name(artistRequest.name())
                .images(artistRequest.images())
                .genres(artistRequest.genres())
                .popularity(artistRequest.popularity())
                .followers(artistRequest.followers())
                .locale(localeConverter.localeTagToLocale(artistRequest.localeTag()))
                .deleted(artistRequest.deleted())
                .createdAt(searchArtist.getCreatedAt())
//                .updateAt(LocalDateTime.now())
                .build();
    }
}
