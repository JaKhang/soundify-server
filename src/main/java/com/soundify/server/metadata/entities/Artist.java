package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.Genre;
import com.soundify.server.shared.data.Image;
import jakarta.persistence.Entity;

import java.util.Locale;
import java.util.Set;

@Entity
public class Artist {
    Set<Image> images;
    String name;
    Set<Genre> genres;
    int popularity;
    int followers;
    Locale locale;
}
