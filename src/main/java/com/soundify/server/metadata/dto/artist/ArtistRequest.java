package com.soundify.server.metadata.dto.artist;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record ArtistRequest(@NotBlank String name, @NotEmpty Set<Image> images, @NotEmpty Set<Genre> genres,
                            int popularity, int followers, String localeTag, boolean deleted) {
}
