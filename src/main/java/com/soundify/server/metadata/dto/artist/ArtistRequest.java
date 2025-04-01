package com.soundify.server.metadata.dto.artist;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;

import java.util.Set;

public record ArtistRequest(String name, Set<Image> images, Set<Genre> genres,
                            int popularity, int followers, String localeTag, boolean deleted) {
}
