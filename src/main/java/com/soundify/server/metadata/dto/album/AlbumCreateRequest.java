package com.soundify.server.metadata.dto.album;

import com.soundify.server.metadata.dto.track.TrackRequest;
import com.soundify.server.shared.data.AlbumType;
import com.soundify.server.shared.data.Copyright;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

// Must have TrackRequest when creating
public record AlbumCreateRequest(@NotBlank String name, @NotNull AlbumType type, String label, @Min(0) int popularity,
                                 @NotEmpty List<Id> artists, String localeTag, Set<String> notAvailableLocaleTags,
                                 @NotEmpty List<TrackRequest> tracks, boolean explicit, @NotEmpty Set<Genre> genres,
                                 @NotEmpty Set<Image> images, Set<Copyright> copyrights, boolean deleted) {
}
