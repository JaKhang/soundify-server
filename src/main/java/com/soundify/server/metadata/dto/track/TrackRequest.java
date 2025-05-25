package com.soundify.server.metadata.dto.track;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record TrackRequest(@NotBlank String name,
                           @Min(0) long duration,
                           boolean explicit,
                           boolean playable,
                           @Min(0) int popularity,
                           @NotNull Id albumId,
                           @NotEmpty List<Id> artistIds,
                           @NotEmpty Set<Genre> genres,
                           boolean deleted) {
}
