package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.AlbumType;
import com.soundify.server.shared.data.Copyright;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.*;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "album_metadata")
@Getter
public class Album extends AbstractEntity {
    String name;
    LocalDateTime releaseDate;
    @Enumerated(EnumType.STRING)
    AlbumType type;
    String label;
    int popularity;
    @ManyToMany
    List<Artist> artists = new ArrayList<>();
    Locale locale;
    @ElementCollection
    Set<Locale> notAvailableLocales = Set.of();
    @OneToMany
    List<Track> tracks = new ArrayList<>();
    boolean explicit;
    @ElementCollection
    Set<Genre> genres = new HashSet<>();
    @ElementCollection
    Set<Image> images = new HashSet<>();
    @ElementCollection
    Set<Copyright> copyrights = new HashSet<>();
}
