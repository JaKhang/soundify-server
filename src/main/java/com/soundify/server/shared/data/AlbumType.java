package com.soundify.server.shared.data;

public enum AlbumType {
    COMPILATION("compilation"),
    ALBUM("album"),
    SINGLE("single");

    private final String value;

    AlbumType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AlbumType fromValue(String value) {
        for (AlbumType type : AlbumType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown album type: " + value);
    }
}