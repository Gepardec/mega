package com.gepardec.mega.monthlyreport.journey;

import com.gepardec.mega.monthlyreport.exception.EnumConverterException;

import java.util.Map;
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

    public static JourneyDirection fromString(String direction) {
        JourneyDirection journeyDirection = enumMap.get(direction);
        if (journeyDirection == null) {
            throw new EnumConverterException(String.format("Error mapping %s to Enum JourneyDirection", direction));
        }
        return journeyDirection;
    }

}
