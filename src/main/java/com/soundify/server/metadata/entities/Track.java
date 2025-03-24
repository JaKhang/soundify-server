package com.soundify.server.metadata.entities;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Track {
    long duration;
    boolean explicit;
    boolean playable;
    boolean popularity;
    String name;
    Album album;
    List<Artist> tracks = new ArrayList<>();
}
