package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.album.AlbumRequest;
import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.album.AlbumUpdateRequest;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.Valid;

import java.util.List;

public interface AlbumService {
    AlbumResponse getById(Id id);

    List<AlbumResponse> getByIds(List<Id> cids);

    Id create(AlbumRequest albumRequest);

    void update(Id id, @Valid AlbumUpdateRequest albumUpdateRequest);

    void delete(Id id);
}
