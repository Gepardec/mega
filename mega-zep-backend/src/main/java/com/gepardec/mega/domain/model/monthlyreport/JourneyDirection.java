package com.gepardec.mega.domain.model.monthlyreport;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum JourneyDirection {
    TO_AIM("0"),
    FURTHER("1"),
    BACK("2");

    private String direction;

    JourneyDirection(String direction) {
        this.direction = direction;
    }

    private static Map<String, JourneyDirection> enumMap = Stream.of(JourneyDirection.values())
            .collect(Collectors.toMap(journeyDirection -> journeyDirection.direction, Function.identity()));

    public static Optional<JourneyDirection> fromString(String direction) {
        return Optional.ofNullable(enumMap.get(StringUtils.defaultIfEmpty(direction, StringUtils.EMPTY).toUpperCase()));
    }

    public String getDirection() {
        return direction;
    }
}
