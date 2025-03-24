package com.soundify.server.shared.data;

import jakarta.persistence.Embeddable;

@Embeddable
public record Copyright(
        String text,
        String type
) {
}
