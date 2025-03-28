package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Builder
@Table(name = "track_metadata")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Track extends AbstractEntity {

    long duration;
    boolean explicit;
    boolean playable;
    int popularity;

    @Column(nullable = false)
    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id")
    Album album;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "track_artist", joinColumns = @JoinColumn(name = "track_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
    List<Artist> artists = new ArrayList<>();

    @ElementCollection
    Set<Genre> genres = new HashSet<>();

}
