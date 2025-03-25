package com.soundify.server.metadata.dto.album;

import com.soundify.server.metadata.dto.SimplifiedArtist;
import com.soundify.server.metadata.dto.image.ImageResponse;
import com.soundify.server.shared.data.AlbumType;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.domain.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record SimplifiedAlbum(Id id, String name, LocalDateTime releaseDate, AlbumType type,
                              String label, List<SimplifiedArtist> artists, boolean explicit,
                              Set<Genre> genres, Set<ImageResponse> images) {
}
