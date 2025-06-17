package com.soundify.server.metadata.repositories;

import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Id> {

    @Query("""
        SELECT ar FROM Artist ar
        WHERE ar.name LIKE CONCAT('%', :query, '%')
        """)
    Page<Artist> searchArtists(@Param("query") String query, Pageable pageable);
}
