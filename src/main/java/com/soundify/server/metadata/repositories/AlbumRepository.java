package com.soundify.server.metadata.repositories;

import com.soundify.server.metadata.entities.Album;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Id> {
}
