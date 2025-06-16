package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.AlbumType;
import com.soundify.server.shared.data.Copyright;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.*;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "album_metadata")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Album extends AbstractEntity {
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    LocalDateTime releaseDate;
    @Enumerated(EnumType.STRING)
    AlbumType type;
    String label;
    @ColumnDefault("0")
    int popularity;
    @ManyToMany
    @JoinTable(name = "album_artist", joinColumns = @JoinColumn(name = "album_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
    List<Artist> artists = new ArrayList<>();
    @Column(nullable = false)
    Locale locale;
    @ElementCollection
    Set<Locale> notAvailableLocales = new HashSet<>();
    @OneToMany(mappedBy = "album", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Track> tracks = new ArrayList<>();
    @ColumnDefault("false")
    boolean explicit;
    @ElementCollection
    Set<Genre> genres = new HashSet<>();
    @ElementCollection
    Set<Image> images = new HashSet<>();
    @ElementCollection
    Set<Copyright> copyrights = new HashSet<>();
}
