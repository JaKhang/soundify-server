package com.soundify.server.metadata.service;

import com.soundify.server.metadata.dto.artist.ArtistRequest;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.shared.domain.Id;

import java.util.List;

public interface ArtistService {
    ArtistResponse getById(Id id);

    List<ArtistResponse> getByIds(List<Id> ids);

    Id create(ArtistRequest artistRequest);

    void update(Id id, ArtistRequest artistRequest);

    void delete(Id id);
}
