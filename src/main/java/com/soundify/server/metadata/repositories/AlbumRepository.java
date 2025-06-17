package com.soundify.server.metadata.repositories;

import com.soundify.server.metadata.entities.Album;
import com.soundify.server.shared.domain.Id;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Id> {
    @Override
    Optional<Album> findById(@NotNull Id id);

    @Query("""
        SELECT DISTINCT a FROM Album a
        LEFT JOIN FETCH a.artists art
        LEFT JOIN FETCH art.images
        LEFT JOIN FETCH art.genres
        LEFT JOIN FETCH a.genres
        LEFT JOIN FETCH a.images
        LEFT JOIN FETCH a.copyrights
        LEFT JOIN FETCH a.notAvailableLocales
        WHERE a.name LIKE CONCAT('%', :query, '%')
        """)
    Page<Album> findAlbumsWithArtists(@Param("query") String query, Pageable pageable);

    @Query("""
        SELECT a FROM Album a
        JOIN a.tracks t
        WHERE t.id = :trackId
        """)
    Optional<Album> findByTrackId(@Param("trackId") Id trackId);

    @Query("""
        SELECT DISTINCT a FROM Album a
        LEFT JOIN FETCH a.artists
        WHERE a.id IN (
            SELECT ca.id FROM Category c
            JOIN c.albums ca
            WHERE c.id = :id
        )
        """)
    Page<Album> findByCategoryId(@Param("id") Id id, Pageable pageable);
}
