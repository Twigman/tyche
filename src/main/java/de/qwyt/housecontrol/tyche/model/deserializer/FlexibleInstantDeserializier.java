package de.qwyt.housecontrol.tyche.model.deserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FlexibleInstantDeserializier extends JsonDeserializer<Instant> {

    // Supported formats (ISO 8601-compliant)
    private static final List<DateTimeFormatter> SUPPORTED_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    );

    // Default timezone for timestamps without explicit timezone information (Assuming UTC)
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    // Regex for detecting timezone offsets (e.g., +01:00, -02:00, or Z)
    private static final Pattern TIMEZONE_PATTERN = Pattern.compile(".*([+-]\\d{2}:\\d{2}|Z)$");

    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        String dateString = parser.getText().trim();

        // Handle special cases like null, empty string, or "none"
        if (dateString == null || dateString.isEmpty() || "none".equalsIgnoreCase(dateString)) {
            return null;
        }

        try {
            // If the timestamp contains a timezone offset or ends with 'Z' (UTC)
            if (TIMEZONE_PATTERN.matcher(dateString).matches()) {
                return Instant.parse(dateString);
            }

            // Assume UTC for timestamps without timezone information
            for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
                try {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
                 // Convert to UTC
                    return localDateTime.atZone(DEFAULT_ZONE).toInstant();
                } catch (DateTimeParseException ignored) {
                    // Try the next format if parsing fails
                }
            }
        } catch (DateTimeParseException e) {
            throw new InvalidFormatException(parser, "Invalid date format: " + dateString, dateString, Instant.class);
        }

        // If no format matches, throw a descriptive exception
        throw new InvalidFormatException(parser, "Unable to parse date: " + dateString, dateString, Instant.class);
    }
}
