package com.soundify.server.metadata.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface LocaleConverter {
    @Named("localeToLocalTag")
    default String localeToLocalTag(Locale locale) {
        return locale != null ? locale.toString() : Locale.ROOT.toLanguageTag();
    }

    @Named("localeTagToLocale")
    default Locale localeTagToLocale(String localeTag) {
        if (localeTag == null || localeTag.isEmpty()) {
            return Locale.ROOT;
        }

        // Xử lý định dạng vi_VN
        if (localeTag.contains("_")) {
            String[] parts = localeTag.split("_", 2);
            return Locale.of(parts[0], parts.length > 1 ? parts[1] : "");
        }

        // Trường hợp chỉ có language code (ví dụ: "vi")
        return Locale.of(localeTag, "");
    }

    @Named("notAvailableToNotAvailableTag")
    default Set<String> notAvailableToNotAvailableTag(Set<Locale> notAvailableLocales) {
        return notAvailableLocales.stream()
                .map(Locale::toString)
                .collect(Collectors.toSet());
    }
}
