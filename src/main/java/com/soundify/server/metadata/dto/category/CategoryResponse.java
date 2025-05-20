package com.soundify.server.metadata.dto.category;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;

import java.util.Set;

public record CategoryResponse(Id id, String name, Set<Image> icons, String localeTag,
                               Set<AlbumResponse> albums, int orderBy) {
}
