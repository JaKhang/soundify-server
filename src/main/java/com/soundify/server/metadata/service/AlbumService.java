package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.shared.domain.Id;

import java.util.List;

public interface AlbumService {
    AlbumResponse getById(Id id);
    List<AlbumResponse> getByIds(List<Id> cids);
}
