package com.soundify.server.metadata.mappers;

import com.soundify.server.metadata.dto.album.SimplifiedAlbum;
import com.soundify.server.metadata.entities.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class, ImageMapper.class})
public interface AlbumMapper {
    SimplifiedAlbum toSimplifiedAlbum(Album album);
}
