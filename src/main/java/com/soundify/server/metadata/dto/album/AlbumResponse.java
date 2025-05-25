package com.soundify.server.metadata.dto.album;

import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.shared.data.AlbumType;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record AlbumResponse(Id id, String name, LocalDateTime releaseDate, AlbumType type, String label,
                            int popularity, List<ArtistResponse> artists, String localeTag,
                            Set<String> notAvailableLocaleTags, boolean explicit, Set<Genre> genres, Set<Image> images) {
}
