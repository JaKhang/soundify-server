package com.soundify.server.metadata.dto.track;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.domain.Id;

import java.util.List;
import java.util.Set;

public record TrackResponse(
        Id id,
        String name,
        long duration,
        boolean explicit,
        boolean playable,
        int popularity,
        AlbumResponse album,
        List<ArtistResponse> artists,
        Set<Genre> genres
) {
}
