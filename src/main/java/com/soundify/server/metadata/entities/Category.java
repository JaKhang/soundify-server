package com.soundify.server.metadata.entities;

import com.soundify.server.metadata.converter.LocaleConverter;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "category")
@Getter
public class Category extends AbstractEntity {

    @ElementCollection
    Set<Image> icon = new HashSet<>();

    @Column(nullable = false)
    String name;

    @Column
    @Convert(converter = LocaleConverter.class)
    Locale locale;

    @ManyToMany
    @JoinTable(name = "category_album", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "album_id"))
    Set<Album> categories = new HashSet<>();

    @ColumnDefault("0")
    int orderBy;
}
