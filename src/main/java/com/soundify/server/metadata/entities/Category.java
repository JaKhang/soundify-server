package com.soundify.server.metadata.entities;

import com.soundify.server.shared.data.Image;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Category {
    Set<Image> icon = new HashSet<Image>();
    String name;
    Locale locale;
}
