package org.saccharine.grabber.utils;

import java.time.LocalDateTime;

public interface AppDateTimeParser {
    LocalDateTime parse(String parse);
}