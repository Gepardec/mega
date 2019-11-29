package com.gepardec.mega.monthlyreport;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
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

    public static WorkingLocation fromString(String value) {
        String keyValue = StringUtils.defaultIfBlank(value, "").toUpperCase();
        WorkingLocation workingLocation = enumMap.get(keyValue);
        if (workingLocation == null) {
            throw new EnumConverterException(String.format("Error mapping %s to Enum JourneyDirection", workingLocation));
        }
        return workingLocation;
    }

}
