package com.soundify.server.metadata.dto.album;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.shared.data.AlbumType;
import com.soundify.server.shared.data.Copyright;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record AlbumRequest(String name, LocalDateTime releaseDate, AlbumType type, String label, int popularity,
                           List<ArtistRequest> artists, String localeTag, Set<String> notAvailableLocaleTags,
                           List<TrackRequest> tracks, boolean explicit, Set<Genre> genres,
                           Set<Image> images, Set<Copyright> copyrights, boolean deleted) {
}
