package com.soundify.server.shared.config;



import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.soundify.server.shared.domain.Id;

import java.io.IOException;

public class IdDeserializer extends JsonDeserializer<Id> {

    @Override
    public Id deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return Id.from(jsonParser.getValueAsString());
    }
}
