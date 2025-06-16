package com.soundify.server.metadata.mappers.decorators;

import com.soundify.server.metadata.dto.album.AlbumRequest;
import com.soundify.server.metadata.dto.album.AlbumUpdateRequest;
import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.AlbumMapper;
import com.soundify.server.metadata.mappers.LocaleConverter;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AlbumMapperDecorator implements AlbumMapper {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    LocaleConverter localeConverter;

    @Override
    public Album createAlbumFromRequest(AlbumRequest request) {
        List<Artist> artists = artistRepository.findAllById(request.artistIds());
        List<TrackRequest> trackRequests = request.tracks();
        List<Track> tracks = new ArrayList<>();
        Id albumId = Id.fast();

        // Setup Album (tracks empty)
        Album album = Album.builder()
                .id(albumId)
                .name(request.name())
                .releaseDate(request.releaseDate())
                .type(request.type())
                .label(request.label())
                .popularity(request.popularity())
                .artists(artists)
                .locale(localeConverter.localeTagToLocale(request.localeTag()))
                .notAvailableLocales(request.notAvailableLocaleTags()
                        .stream()
                        .map(localeConverter::localeTagToLocale)
                        .collect(Collectors.toSet()))
                .tracks(tracks)
                .explicit(request.explicit())
                .genres(request.genres())
                .images(request.images())
                .deleted(request.deleted())
                .build();

        // Create new Track base on albumId, update each of track in tracks (reference)
        trackRequests.forEach(trackRequest -> {
            Track track = Track.builder()
                    .id(Id.fast())
                    .name(trackRequest.name())
                    .duration(trackRequest.duration())
                    .explicit(trackRequest.explicit())
                    .playable(trackRequest.playable())
                    .popularity(trackRequest.popularity())
                    .album(album)
                    .artists(artists)
                    .genres(trackRequest.genres())
                    .deleted(trackRequest.deleted())
                    .build();

            tracks.add(track);
        });

        return album;
    }

    @Override
    public Album updateAlbumFromRequest(Id id, AlbumUpdateRequest request) {
        Album existingAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));

        List<Artist> artists = artistRepository.findAllById(request.artistIds());

        List<Track> tracks = trackRepository.findAllById(request.trackIds());

        Set<Locale> notAvailableLocales = existingAlbum.getNotAvailableLocales();
        notAvailableLocales.clear();
        notAvailableLocales.addAll(request.notAvailableLocaleTags()
                .stream()
                .map(localeConverter::localeTagToLocale)
                .collect(Collectors.toSet()));

        return Album.builder()
                .id(existingAlbum.getId())  // Giữ nguyên ID
                .name(request.name())
                .releaseDate(request.releaseDate())
                .type(request.type())
                .label(request.label())
                .popularity(request.popularity())
                .artists(artists)
                .locale(localeConverter.localeTagToLocale(request.localeTag()))
                .notAvailableLocales(notAvailableLocales)
                .tracks(tracks)  // Danh sách tracks rỗng, sẽ được cập nhật sau
                .explicit(request.explicit())
                .genres(request.genres())
                .images(request.images())
                .deleted(request.deleted())
                .createdAt(existingAlbum.getCreatedAt())
                .build();
    }
}
