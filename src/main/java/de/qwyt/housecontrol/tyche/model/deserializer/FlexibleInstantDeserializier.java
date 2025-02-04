package de.qwyt.housecontrol.tyche.model.deserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FlexibleInstantDeserializier extends JsonDeserializer<Instant> {
	
	private static final DateTimeFormatter FORMATTER_WITH_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
    private static final DateTimeFormatter FORMATTER_WITHOUT_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	@Override
	public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		String dateString = p.getText().trim();
		
		if (dateString.equals("none")) {
			// DimmerSwitch
			return null;
		}
		
		try {
            return Instant.parse(dateString); // Standard ISO 8601 mit 'Z' funktioniert direkt
        } catch (DateTimeParseException e) {
            try {
            	LocalDateTime localDateTime = LocalDateTime.parse(dateString, FORMATTER_WITH_MS);
            	return localDateTime.toInstant(ZoneOffset.UTC);
            } catch (DateTimeParseException ex) {
            	LocalDateTime localDateTime = LocalDateTime.parse(dateString, FORMATTER_WITHOUT_MS);
                return localDateTime.toInstant(ZoneOffset.UTC);
            }
        }
	}

}
