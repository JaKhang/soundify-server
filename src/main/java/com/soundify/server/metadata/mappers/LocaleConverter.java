package com.soundify.server.metadata.mappers;

import org.mapstruct.Named;

import java.util.Locale;

public interface LocaleConverter {
    @Named("localeToLocalTag")
    default String localeToLocalTag(Locale locale) {
        return locale != null ? locale.toLanguageTag() : Locale.ROOT.toLanguageTag();
    }

    @Named("localeTagToLocale")
    default Locale localeTagToLocale(String localeTag) {
        return localeTag == null ? Locale.ROOT : Locale.forLanguageTag(localeTag);
    }
}
