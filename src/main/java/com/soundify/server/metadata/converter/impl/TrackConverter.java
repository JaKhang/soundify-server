package com.soundify.server.metadata.converter.impl;

import com.soundify.server.metadata.converter.Converter;
import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrackConverter implements Converter<Track, TrackRequest> {
    AlbumRepository albumRepository;
    ArtistRepository artistRepository;

    @Override
    public Track toEntity(TrackRequest request) {
        Album album = albumRepository.findById(request.albumId())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));
        List<Artist> artists = artistRepository.findAllById(request.artistIds());

        if (artists.size() != request.artistIds().size()) {
            throw new ResourceNotFoundException("Some artists in list are not exists");
        }

        return Track.builder()
                .id(Id.from(request.id()))
                .name(request.name())
                .duration(request.duration())
                .explicit(request.explicit())
                .playable(request.playable())
                .popularity(request.popularity())
                .album(album)
                .artists(artists)
                .genres(request.genres())
                .build();
    }

    @Override
    public List<Track> toEntities(List<TrackRequest> request) {
        return List.of();
    }
}
