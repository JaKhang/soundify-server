package com.soundify.server.metadata.mappers.decorators;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class TrackMapperDecorator implements TrackMapper {

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Override
    public Track updateTrackFromRequest(Id id, TrackRequest trackRequest) {
        // Call to get createdAt
        Track existingTrack = trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        Album album = albumRepository.findById(trackRequest.albumId())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));
        List<Artist> artists = artistRepository.findAllById(trackRequest.artistIds());
        Set<Genre> genres = new HashSet<>(trackRequest.genres());

        return Track.builder()
                .id(existingTrack.getId())
                .name(trackRequest.name())
                .duration(trackRequest.duration())
                .explicit(trackRequest.explicit())
                .playable(trackRequest.playable())
                .popularity(trackRequest.popularity())
                .album(album)
                .artists(artists)
                .genres(genres)
                .deleted(trackRequest.deleted())
                .createdAt(existingTrack.getCreatedAt())
//                .updateAt(LocalDateTime.now())
                .build();
    }
}
