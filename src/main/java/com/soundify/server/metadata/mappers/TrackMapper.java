package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Track;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class, ArtistMapper.class})
public interface TrackMapper {
    TrackResponse toTrackResponse(Track track);
    List<TrackResponse> toTrackResponses(List<Track> tracks);
}
