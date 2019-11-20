package com.gepardec.mega.monthendreport;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum JourneyDirection {
    TO_AIM(0),
    FURTHER(1),
    BACK(2);

    private Integer direction;

    JourneyDirection(Integer direction) {
        this.direction = direction;
    }

    private static Map<Integer, JourneyDirection> enumMap = Stream.of(JourneyDirection.values())
            .collect(Collectors.toMap(journeyDirection -> journeyDirection.direction, Function.identity()));

    public static Optional<JourneyDirection> fromInteger(Integer direction) {
        return Optional.of(enumMap.get(direction));
    }

}
