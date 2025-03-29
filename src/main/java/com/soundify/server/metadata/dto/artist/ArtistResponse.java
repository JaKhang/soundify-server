package com.soundify.server.metadata.dto.artist;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;

import java.util.Set;

public record ArtistResponse(Id id, String name, Set<Image> images, Set<Genre> genres,
                             int popularity, int followers, String localeTag) {
}
