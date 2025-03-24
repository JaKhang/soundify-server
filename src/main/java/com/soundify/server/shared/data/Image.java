package com.soundify.server.shared.data;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class Image {
    String url;
    String height;
    int width;
}
