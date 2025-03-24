package com.soundify.server.shared.data;

public enum Genre {
    POP("pop"),
    ROCK("rock"),
    JAZZ("jazz"),
    BLUES("blues"),
    CLASSICAL("classical"),
    HIP_HOP("hip-hop"),
    RAP("rap"),
    RNB("rnb"),
    COUNTRY("country"),
    ELECTRONIC("electronic"),
    HOUSE("house"),
    TECHNO("techno"),
    TRANCE("trance"),
    DUBSTEP("dubstep"),
    DRUM_AND_BASS("drum-and-bass"),
    FOLK("folk"),
    REGGAE("reggae"),
    SKA("ska"),
    PUNK("punk"),
    METAL("metal"),
    HEAVY_METAL("heavy-metal"),
    ALTERNATIVE("alternative"),
    INDIE("indie"),
    FUNK("funk"),
    SOUL("soul"),
    GOSPEL("gospel"),
    OPERA("opera"),
    SOUNDTRACK("soundtrack"),
    LATIN("latin"),
    SALSA("salsa"),
    SAMBA("samba"),
    BACHATA("bachata"),
    K_POP("k-pop"),
    J_POP("j-pop"),
    C_POP("c-pop"),
    AFROBEATS("afrobeats"),
    WORLD("world"),
    LOFI("lofi"),
    AMBIENT("ambient"),
    CHILL("chill"),
    GRIME("grime"),
    DANCEHALL("dancehall");

    private final String value;

    Genre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Genre fromValue(String value) {
        for (Genre genre : Genre.values()) {
            if (genre.value.equalsIgnoreCase(value)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre: " + value);
    }
}