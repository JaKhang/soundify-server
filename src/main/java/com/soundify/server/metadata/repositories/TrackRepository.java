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
    @Query("""
            SELECT t FROM Track t
            LEFT JOIN FETCH t.artists ta
            LEFT JOIN FETCH t.album
            WHERE t.album.id = :albumId
            """)
    List<Track> findByAlbumId(@Param("albumId") Id albumId);

    @Query("""
        SELECT DISTINCT t FROM Track t
        LEFT JOIN FETCH t.album a
        LEFT JOIN FETCH t.artists ar
        WHERE t.name LIKE CONCAT('%', :query, '%')
        """)
    Page<Track> searchTracks(@Param("query") String query, Pageable pageable);

    @Query("""
            SELECT t FROM Track t
            LEFT JOIN FETCH t.artists ta
            LEFT JOIN FETCH t.album
            WHERE ta.id = :id
            """)
    List<Track> findByArtistId(Id id);
}
