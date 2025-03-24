package com.soundify.server.shared.data;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;

@Embeddable
public record Copyright(
        String text,
        String type
) {
}
