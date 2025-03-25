package com.soundify.server.metadata.dto;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;

import java.util.Set;

public record SimplifiedArtist(Id id, String name, Set<Image> images, Set<Genre> genres,
                               int popularity, int followers, String localeTag) {
}
