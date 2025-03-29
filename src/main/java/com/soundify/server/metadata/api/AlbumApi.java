package com.soundify.server.metadata.api;

import com.soundify.server.metadata.dto.ApiResponse;
import com.soundify.server.metadata.dto.album.AlbumResponse;
import com.soundify.server.metadata.service.AlbumService;
import com.soundify.server.shared.domain.Id;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/albums")
public class AlbumApi {
    AlbumService albumService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumResponse>> getAlbumById(@PathVariable String id) {
        Id cid = Id.from(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", albumService.getById(cid)));
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<ApiResponse<List<AlbumResponse>>> getAlbumByIds(@PathVariable String ids) {
        String[] decodeIds = URLDecoder.decode(ids, StandardCharsets.UTF_8).split(",");
        List<Id> cids = Arrays.stream(decodeIds)
                .map(Id::from)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", albumService.getByIds(cids)));
    }
}
