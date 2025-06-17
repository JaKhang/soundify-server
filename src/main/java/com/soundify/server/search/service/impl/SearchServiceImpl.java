package com.soundify.server.search.service.impl;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.artist.ArtistResponse;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Artist;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.AlbumMapper;
import com.soundify.server.metadata.mappers.ArtistMapper;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.repositories.ArtistRepository;
import com.soundify.server.metadata.repositories.TrackRepository;
import com.soundify.server.search.dto.SearchRequest;
import com.soundify.server.search.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    TrackRepository trackRepository;
    AlbumRepository albumRepository;
    ArtistRepository artistRepository;
    TrackMapper trackMapper;
    AlbumMapper albumMapper;
    ArtistMapper artistMapper;

    @Override
    public List<TrackResponse> searchTracks(SearchRequest searchRequest) {
        String query = searchRequest.getCleanQuery();
        // Đã handle query = null hoặc query = "" trong getCleanQuery
        if (query == null) return List.of();
        Pageable pageable = searchRequest.toPageable();
        Page<Track> tracks = trackRepository.searchTracks(query, pageable);
        return trackMapper.toTrackResponses(tracks.getContent());
    }

    @Transactional
    @Override
    public List<AlbumResponse> searchAlbums(SearchRequest searchRequest) {
        String query = searchRequest.getCleanQuery();
        if (query == null) return List.of();
        Pageable pageable = searchRequest.toPageable();
        // Chỉ lấy albums với artists
        List<Album> albums = albumRepository.findAlbumsWithArtists(query, pageable).getContent();

        return albumMapper.toAlbumResponses(albums);
    }

    @Override
    public List<ArtistResponse> searchArtists(SearchRequest searchRequest) {
        String query = searchRequest.getCleanQuery();
        if (query == null) return List.of();
        Pageable pageable = searchRequest.toPageable();
        Page<Artist> artists = artistRepository.searchArtists(query, pageable);
        return artistMapper.toArtistResponses(artists.getContent());
    }
}
