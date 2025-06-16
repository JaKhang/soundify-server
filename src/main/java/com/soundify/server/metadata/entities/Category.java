package com.soundify.server.metadata.entities;

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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "category_metadata")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category extends AbstractEntity {

    @ElementCollection
    Set<Image> icons = new HashSet<>();

    @Column(nullable = false)
    String name;

    @Column
    Locale locale;

    @ManyToMany
    @JoinTable(name = "category_album", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "album_id"))
    Set<Album> albums = new HashSet<>();

    @ColumnDefault("0")
    int orderBy;
}
