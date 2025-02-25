package de.qwyt.housecontrol.tyche.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeHelper {
	
	// "UTC+1"
    private static final ZoneId ZONE_UTC_PLUS_ONE = ZoneId.of("Europe/Berlin");

    // Startzeit des Tages in UTC+1
    public static Instant getStartOfDayUTCPlus1(LocalDate date) {
        ZonedDateTime zonedStartOfDay = date.atStartOfDay(ZONE_UTC_PLUS_ONE);
        return zonedStartOfDay.toInstant();
    }

    // Endzeit des Tages in UTC+1
    public static Instant getEndOfDayUTCPlus1(LocalDate date) {
        ZonedDateTime zonedEndOfDay = date.plusDays(1).atStartOfDay(ZONE_UTC_PLUS_ONE).minusNanos(1);
        return zonedEndOfDay.toInstant();
    }

}
