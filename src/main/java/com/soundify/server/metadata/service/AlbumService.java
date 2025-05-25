package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.Valid;

import java.util.List;

public interface AlbumService {
    AlbumResponse getById(Id id);

    List<AlbumResponse> getByIds(List<Id> cids);

    Id create(AlbumResponse albumResponse);

    void update(Id id, @Valid AlbumResponse albumResponse);

    void delete(Id id);
}
