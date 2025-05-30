package com.soundify.server.metadata.repositories;

import com.soundify.server.metadata.entities.Track;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Id> {
    List<Track> findByAlbumId(Id albumId);
}
