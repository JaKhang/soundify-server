package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Entity
@Table(name = "category")
@Getter
public class Category extends AbstractEntity {
    Set<Image> icon = new HashSet<Image>();
    String name;
    Locale locale;
}
