package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.NotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class, ArtistMapper.class})
public interface TrackMapper {
    TrackResponse toTrackResponse(Track track);

    List<TrackResponse> toTrackResponses(List<Track> tracks);

    @Mapping(source = "albumId", target = "track.album", qualifiedByName = "albumIdToAlbum")
    @Mapping(source = "artistIds", target = "track.artists", qualifiedByName = "artistIdsToArtists")
    Track toTrack(TrackRequest trackRequest,
                  @Context AlbumRepository albumRepository,
                  @Context ArtistRepository artistRepository);

    @Named("albumIdToAlbum")
    default Album albumIdToAlbum(Id albumId, @Context AlbumRepository albumRepository) {
        if (albumId != null) {
            return albumRepository
                    .findById(albumId)
                    .orElseThrow(() -> new NotFoundException("Album not found"));
        }
        throw new NotFoundException("Album not found");
    }

    @Named("artistIdsToArtists")
    default List<Artist> artistIdsToArtists(List<Id> artistIds, @Context ArtistRepository artistRepository) {
        if (artistIds != null && !artistIds.isEmpty()) {
            return artistRepository.findAllById(artistIds);
        }
        throw new NotFoundException("Artists not found");
    }
}
