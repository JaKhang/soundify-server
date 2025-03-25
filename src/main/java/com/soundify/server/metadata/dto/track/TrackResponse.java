package com.soundify.server.metadata.dto.track;

import com.soundify.server.metadata.dto.SimplifiedArtist;
import com.soundify.server.metadata.dto.album.SimplifiedAlbum;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.domain.Id;

import java.util.List;
import java.util.Set;

public record TrackResponse(Id id, String name, long duration, boolean explicit, boolean playable, int popularity,
                            SimplifiedAlbum album, List<SimplifiedArtist> artists, Set<Genre> genres) {
}
