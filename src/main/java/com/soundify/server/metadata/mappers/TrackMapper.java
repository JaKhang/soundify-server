package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Track;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class, ArtistMapper.class})
public interface TrackMapper {
    TrackResponse toTrackResponse(Track track);
}
