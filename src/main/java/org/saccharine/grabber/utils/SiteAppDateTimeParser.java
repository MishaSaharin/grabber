package org.saccharine.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SiteAppDateTimeParser implements AppDateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(parse, DateTimeFormatter.ISO_DATE_TIME);
    }
}