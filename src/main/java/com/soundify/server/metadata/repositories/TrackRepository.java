package com.soundify.server.metadata.repositories;

import com.soundify.server.metadata.entities.Track;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Id> {
    List<Track> findByAlbumId(Id albumId);

    @Query("""
        SELECT DISTINCT t FROM Track t
        LEFT JOIN FETCH t.album a
        LEFT JOIN FETCH t.artists ar
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(ar.name) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<Track> searchTracks(@Param("query") String query, Pageable pageable);
}
