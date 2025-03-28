package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.BadRequestException;
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

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToId")
    @Mapping(source = "albumId", target = "album", qualifiedByName = "albumIdToAlbum")
    @Mapping(source = "artistIds", target = "artists", qualifiedByName = "artistIdsToArtists")
    Track toTrack(TrackRequest trackRequest,
                  @Context AlbumRepository albumRepository,
                  @Context ArtistRepository artistRepository);

    @Named("albumIdToAlbum")
    default Album albumIdToAlbum(Id albumId, @Context AlbumRepository albumRepository) {
        if (albumId == null) throw new BadRequestException("Album must be included");
        return albumRepository
                .findById(albumId)
                .orElseThrow(() -> new NotFoundException("Album not found"));

    }

    @Named("artistIdsToArtists")
    default List<Artist> artistIdsToArtists(List<Id> artistIds, @Context ArtistRepository artistRepository) {
        if (artistIds == null || !artistIds.isEmpty()) throw new NotFoundException("Artist must be included");
        return artistRepository.findAllById(artistIds);
    }

    @Named("stringToId")
    default Id stringToId(String id) {
        if (id == null) return null;
        return Id.from(id);
    }
}
