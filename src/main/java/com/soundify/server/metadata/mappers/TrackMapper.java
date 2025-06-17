package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.decorators.TrackMapperDecorator;
import com.soundify.server.shared.domain.Id;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class, ArtistMapper.class, LocaleConverter.class})
@DecoratedWith(TrackMapperDecorator.class)
public interface TrackMapper {

    TrackResponse toTrackResponse(Track track);

    List<TrackResponse> toTrackResponses(List<Track> tracks);

    Track updateTrackFromRequest(Id id, TrackRequest trackRequest);
}
