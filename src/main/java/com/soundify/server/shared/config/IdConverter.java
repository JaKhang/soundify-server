package com.soundify.server.shared.config;

import com.soundify.server.shared.domain.Id;
import org.springframework.core.convert.converter.Converter;

public class IdConverter implements Converter<String, Id> {
    @Override
    public Id convert(String source) {
        return Id.from(source);
    }
}
