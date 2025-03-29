package com.soundify.server.metadata.entities;

import com.soundify.server.metadata.converter.LocaleConverter;
import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.Locale;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "artist_metadata")
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
    @Convert(converter = LocaleConverter.class)
    Locale locale;
}
