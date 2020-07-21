package com.gepardec.mega.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WorkingLocation {
    DEFAULT_WORKING_LOCATION("- erste Tätigkeitsstätte -"),
    AUSTRIA("A"),
    GERMANY("D"),
    SWITZERLAND("SCHWEIZ"),
    CZECH("CZ");

    private String workingLocationCode;

    WorkingLocation(String workingLocationCode) {
        this.workingLocationCode = workingLocationCode;
    }

    private static Map<String, WorkingLocation> enumMap = Stream.of(WorkingLocation.values())
            .collect(Collectors.toMap(e -> e.workingLocationCode.toUpperCase(), Function.identity()));

    public static Optional<WorkingLocation> fromString(String value) {
        return Optional.ofNullable(enumMap.get(StringUtils.defaultIfBlank(value, StringUtils.EMPTY).toUpperCase()));
    }

    public String getWorkingLocationCode() {
        return workingLocationCode;
    }
}
