package com.soundify.server.metadata.repositories;

import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Id> {
}
