package com.soundify.server.metadata.service.impl;

import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.dto.track.TrackResponse;
import com.soundify.server.metadata.entities.Album;
import com.soundify.server.metadata.entities.Track;
import com.soundify.server.metadata.mappers.AlbumMapper;
import com.soundify.server.metadata.mappers.TrackMapper;
import com.soundify.server.metadata.repositories.AlbumRepository;
import com.soundify.server.metadata.service.AlbumService;
import com.soundify.server.metadata.service.TrackService;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumServiceImpl implements AlbumService {
    AlbumRepository albumRepository;
    AlbumMapper albumMapper;
    TrackService trackService;
    TrackMapper trackMapper;

    @Override
    public AlbumResponse getById(Id id) {
        return albumMapper.toAlbumResponse(albumRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found")));
    }

    @Override
    public List<AlbumResponse> getByIds(List<Id> cids) {
        return albumMapper.toAlbumResponses(albumRepository
                .findAllById(cids));
    }

    @Override
    public Id create(AlbumResponse albumResponse) {
        return null;
    }

    @Override
    public void update(Id id, AlbumResponse albumResponse) {

    }

    @Override
    public void delete(Id id) {

    }

    @Override
    public List<TrackResponse> getTracks(Id id) {
        return trackMapper.toTrackResponses(trackService.findByAlbumId(id));
    }

    @Override
    public List<AlbumResponse> getByCategoryId(Id id, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "popularity");
        Pageable pageable = PageRequest.of(page, size, sort);
        return albumMapper.toAlbumResponses(albumRepository.findByCategoryId(id, pageable).getContent());
    }

    @Override
    public AlbumResponse getByTrackId(Id id) {
        Optional<Album> optAlbum = albumRepository.findByTrackId(id);
        if (optAlbum.isEmpty()) throw new ResourceNotFoundException("Album not found in system");

        Album album = optAlbum.get();
        List<Track> tracks = trackService.findByAlbumId(album.getId());
        album.setTracks(tracks);
        return albumMapper.toAlbumResponse(optAlbum.get());
    }
}
