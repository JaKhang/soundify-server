package com.soundify.server.metadata.entities;

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

import java.util.Locale;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "artist_metadata")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Artist extends AbstractEntity {

    String name;

    @ElementCollection
    Set<Image> images;

    @ElementCollection
    Set<Genre> genres;

    @ColumnDefault("0")
    int popularity;

    @ColumnDefault("0")
    int followers;

    @Column(nullable = false)
    Locale locale;
}
