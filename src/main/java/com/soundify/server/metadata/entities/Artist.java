package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "artist_metadata")
@Getter
public class Artist extends AbstractEntity {
    Set<Image> images;
    String name;
    Set<Genre> genres;
    int popularity;
    int followers;
    Locale locale;
}
